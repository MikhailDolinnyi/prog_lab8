package ru.mikhail.gui;


import ru.mikhail.gui.actions.UpdateAction;
import ru.mikhail.models.Coordinates;
import ru.mikhail.models.SpaceMarine;
import ru.mikhail.network.Request;
import ru.mikhail.network.Response;
import ru.mikhail.network.ResponseStatus;
import ru.mikhail.network.User;
import ru.mikhail.utility.Client;

import javax.imageio.ImageIO;
import javax.swing.Timer;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.*;

class CartesianPanel extends JPanel implements ActionListener {
    private final Client client;
    private final User user;
    private LinkedHashMap<Rectangle, Integer> rectangles = new LinkedHashMap<>();
    private final Timer timer;
    private Map<String, Color> users;
    private int step;
    private Collection<SpaceMarine> collection;

    private double maxCordX;
    private Double maxCordY;
    private final BufferedImage img;
    private boolean isDragging = false;
    private boolean skip_animation = false;

    private Point mouseDragOldPoint; // Переменная для drag`n`drop
    private Rectangle mouseDragRectangle;
    private SpaceMarine mouseDragObject;

    {
        try {
            this.img = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResource("icons/vip.jpg")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public CartesianPanel(Client client, User user, GuiManager guiManager) {
        super();
        this.client = client;
        this.user = user;
        this.step = 0;
        this.timer = new Timer(1, this);
        timer.start();
        updateUserColors();
        // Изменение по дабл клику
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() != 2) return;
                Rectangle toClick;
                try {
                    toClick = rectangles.keySet().stream()
                            .filter(r -> r.contains(e.getPoint()))
                            .sorted(Comparator.comparing(Rectangle::getX).reversed())
                            .toList().get(0);
                } catch (ArrayIndexOutOfBoundsException k) {
                    return;
                }
                Long id = Long.valueOf(rectangles.get(toClick));
                new UpdateAction(user, client, guiManager).updateJOptionWorker(id);
            }
        });
        // Drag`n`drop объектов
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                try {
                    mouseDragOldPoint = e.getPoint();
                    mouseDragRectangle = rectangles.keySet().stream()
                            .filter(r -> r.contains(mouseDragOldPoint))
                            .sorted(Comparator.comparing(Rectangle::getX).reversed())
                            .toList().get(0);
                    Long id = Long.valueOf(rectangles.get(mouseDragRectangle));
                    mouseDragObject = collection.stream()
                            .filter((s) -> s.getId().equals(id))
                            .toList().get(0);
                    if (!mouseDragObject.getUserLogin().equals(user.name())) return;
                    isDragging = true;
                } catch (ArrayIndexOutOfBoundsException ignored) {
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (!mouseDragObject.getUserLogin().equals(user.name())) return;
                super.mouseReleased(e);
                if (!isDragging) return;
                int width = getWidth();
                int halfWidth = width / 2;
                int height = getHeight();
                int halfHeight = height / 2;
                int elementWidth = 130;
                int elementHeight = 130;
                mouseDragObject.setCoordinates(new Coordinates(
                        (maxCordX / (halfWidth - elementWidth)) * (e.getX() - halfWidth),
                        (maxCordY / (halfHeight - elementHeight)) * (e.getY() - halfHeight)));
                client.sendAndAskResponse(new Request("update", String.valueOf(mouseDragObject.getId()), user, mouseDragObject, GuiManager.getLocale()));
                guiManager.repaintNoAnimation();
                mouseDragOldPoint = e.getPoint();
                isDragging = false;
                skip_animation = true;
            }
        });
        this.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseMoved(MouseEvent e) {
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (!mouseDragObject.getUserLogin().equals(user.name())) return;
                int width = getWidth();
                int halfWidth = width / 2;
                int height = getHeight();
                int halfHeight = height / 2;
                int elementWidth = 130;
                int elementHeight = 130;
                collection.stream()
                        .filter(s -> s.getId().equals(mouseDragObject.getId()))
                        .forEach(s -> s.setCoordinates(new Coordinates(
                                (maxCordX / (halfWidth - elementWidth)) * (e.getX() - halfWidth),
                                (maxCordY / (halfHeight - elementHeight)) * (e.getY() - halfHeight))));
                CartesianPanel.this.repaint();
            }
        });
    }

    public void updateUserColors() {
        Random random = new Random();
        Response response = client.sendAndAskResponse(new Request("show", "", user, GuiManager.getLocale()));
        if (response.getStatus() == ResponseStatus.OK) {
            List<SpaceMarine> spaceMarines = (List<SpaceMarine>) response.getCollection();
            Map<String, Color> userColors = new HashMap<>();
            Set<Coordinates> coordinatesSet = new HashSet<>();

            for (SpaceMarine spaceMarine : spaceMarines) {
                String userLogin = spaceMarine.getUserLogin();
                coordinatesSet.add(spaceMarine.getCoordinates());
                if (!userColors.containsKey(userLogin)) {
                    int red = random.nextInt(25) * 10;
                    int green = random.nextInt(25) * 10;
                    int blue = random.nextInt(25) * 10;
                    userColors.put(userLogin, new Color(red, green, blue));
                }
            }

            float delta = 0.2F;
            for (SpaceMarine spaceMarine : spaceMarines) {
                Coordinates coordinates = spaceMarine.getCoordinates();
                if (coordinatesSet.contains(coordinates)) {
                    coordinates.setX(coordinates.getX() + delta);
                    coordinates.setY(coordinates.getY() + delta);
                    coordinatesSet.remove(coordinates);
                    coordinatesSet.add(coordinates); // Обновляем множество координат
                }
            }

            this.users = userColors;
            this.collection = spaceMarines;
            this.maxCordX = spaceMarines.stream()
                    .map(SpaceMarine::getCoordinates)
                    .mapToDouble(Coordinates::getX)
                    .map(Math::abs)
                    .max()
                    .orElse(0D);
            this.maxCordY = spaceMarines.stream()
                    .map(SpaceMarine::getCoordinates)
                    .mapToDouble(Coordinates::getY)
                    .map(Math::abs)
                    .max()
                    .orElse(0D);
        } else {
            collection = new ArrayList<>();
        }
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;


        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.setFont(new Font("Tahoma", Font.BOLD, 45));

        int width = getWidth();
        int height = getHeight();

        // Ось x
        g2.drawLine(0, height / 2, width, height / 2);

        // Ось y
        g2.drawLine(width / 2, 0, width / 2, height);

        // Стрелочки
        g2.drawLine(width - 10, height / 2 - 5, width, height / 2);
        g2.drawLine(width - 10, height / 2 + 5, width, height / 2);
        g2.drawLine(width / 2 - 5, 10, width / 2, 0);
        g2.drawLine(width / 2 + 5, 10, width / 2, 0);
        this.paintRectangles(g2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (step == 100) timer.stop();
        else {
            step += 2;
            step = Math.min(step, 100);
            repaint();
        }
    }

    private void paintRectangles(Graphics2D g2) {
        int width = getWidth();
        int halfWidth = width / 2;
        int height = getHeight();
        int halfHeight = height / 2;
        int elementWidth = 130;
        int elementHeight = 130;
        if (skip_animation) {
            this.step = 100;
            this.skip_animation = false;
        }
        if (step == 100) this.rectangles = new LinkedHashMap<>();
        this.collection.stream().sorted(SpaceMarine::compareTo).forEach(spaceMarine -> {
            int dx1 = (int) ((halfWidth + (spaceMarine.getCoordinates().getX() * step / 100 / maxCordX * (halfWidth - elementWidth))));
            int dx2 = (int) ((halfHeight + (spaceMarine.getCoordinates().getY() * step / 100 / maxCordY * (halfHeight - elementHeight))));
            if (step == 100) {
                this.rectangles.put(new Rectangle(dx1 - elementWidth / 2 - 1,
                        dx2 - elementHeight / 2 - 1,
                        elementWidth + 2,
                        elementHeight + 2), Math.toIntExact(spaceMarine.getId()));
            }
            //Image
            g2.drawImage(img,
                    dx1 - elementWidth / 2,
                    dx2 - elementHeight / 2,
                    dx1 + elementWidth / 2,
                    dx2 + elementHeight / 2,
                    0,
                    0,
                    img.getWidth(),
                    img.getHeight(),
                    null
            );
            //Border
            g2.setColor(users.get(spaceMarine.getUserLogin()));
            g2.drawRect(dx1 - elementWidth / 2 - 1,
                    dx2 - elementHeight / 2 - 1,
                    elementWidth + 2,
                    elementHeight + 2);
            g2.setColor(Color.WHITE);
            //Numbers
            g2.drawString(spaceMarine.getId().toString(),
                    dx1 - elementWidth / 4,
                    dx2 + elementHeight / 4
            );
        });
    }

    public void reanimate() {
        this.step = 0;
        this.timer.start();
    }

    public void reanimate(int step) {
        this.step = 100;
        repaint();
    }

}