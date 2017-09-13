package com.hotmail.solntsev_igor.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * Created by solncevigor on 9/12/17.
 */
public class ImageComparator {
    private IntegerComparator comparator = new IntegerComparator();
    private BufferedImage image1;
    private BufferedImage image2;
    private String imageType = "png";
    private File result = null;
    private int[][] fieldDiff;
    private static final int DIFFERENCE = 10;

//method to compare 2 images
    public void compareImages(File fileA, File fileB, File fileResult) {
        try {
            image1 = ImageIO.read(fileA);
            image2 = ImageIO.read(fileB);
            String[] fileName = fileA.getName().split("\\.");
            imageType = fileName[fileName.length - 1];
            fieldDiff = new int[image1.getWidth()][image1.getHeight()];
            result = fileResult;
            imageScanner();
        } catch (IOException e) {
            System.err.println("File is non-existent/corrupt: " + e);
        }
    }

    //method to scan image
    private void imageScanner() {
        int imageType = image1.getType();
        BufferedImage result = new BufferedImage(image1.getWidth() > image2.getWidth() ? image1.getWidth() : image2.getWidth(), image1.getHeight() > image2.getHeight() ? image1.getHeight() : image2.getHeight(), imageType);
        result.setData(image1.getData());

        for (int x = 0; x < image1.getWidth(); x++) {
            for (int y = 0; y < image1.getHeight(); y++) {
                //check if the rgb is equal
                if (comparator.compare(image1.getRGB(x, y), image2.getRGB(x, y)) != 0) {
                    fieldDiff[x][y] = -1;
                }
            }
        }

        List<DifferenceRectangle> rectangles = new ArrayList<>();
        int differenceMarker = 1;
        for (int i = 0; i < image1.getWidth(); i++) {
            for (int j = 0; j < image1.getHeight(); j++) {
                if (fieldDiff[i][j] == -1) {
                    DifferenceRectangle rectangle = new DifferenceRectangle(differenceMarker++);
                    fieldDiff[i][j] = rectangle.marker;
                    rectangle.addPixel(i, j);
                    rectangles.add(rectangle);
                    drawRectangl(i, j, rectangle);
                }
            }
        }

        for (DifferenceRectangle rectangle : rectangles) {
            Pixel leftPixel = rectangle.getBoundPixel(true);
            Pixel rightPixel = rectangle.getBoundPixel(false);
            Graphics2D graph = result.createGraphics();
            graph.setColor(Color.BLUE);
            Rectangle markedRectangle = new Rectangle();
            markedRectangle.x = leftPixel.getCoordinate_X() - 2;
            markedRectangle.y = leftPixel.getCoordinate_Y() - 2;
            markedRectangle.height = (rightPixel.getCoordinate_Y() - leftPixel.getCoordinate_Y()) + 3;
            markedRectangle.width = (rightPixel.getCoordinate_X() - leftPixel.getCoordinate_X()) + 3;
            graph.draw(markedRectangle);
            graph.dispose();
        }

        try {
            ImageIO.write(result, this.imageType, this.result);
        } catch (IOException e) {
            System.err.println("Rendering went wrong: " + e);
        }
    }

//method to draw rectangle around different area
    private void drawRectangl(int coordinateByX, int coordinateBy_Y, DifferenceRectangle rectangle) {
        int min_X = (coordinateByX - DIFFERENCE)<0?0:coordinateByX- DIFFERENCE;
        int min_Y = (coordinateBy_Y - DIFFERENCE)<0?0:coordinateBy_Y- DIFFERENCE;
        int max_X = (coordinateByX + DIFFERENCE)> image1.getWidth()-1? image1.getWidth():coordinateByX + DIFFERENCE;
        int max_Y = (coordinateBy_Y + DIFFERENCE)> image1.getHeight()-1? image1.getHeight():coordinateBy_Y + DIFFERENCE;

        for (int i = min_X; i < max_X; i++) {
            for (int j = min_Y; j < max_Y; j++) {
                if (fieldDiff[i][j] != 0 && fieldDiff[i][j] != rectangle.marker) {
                    fieldDiff[i][j] = rectangle.marker;
                    rectangle.addPixel(i, j);
                    drawRectangl(i, j, rectangle);
                }
            }
        }
    }

    private class DifferenceRectangle {
        private ArrayList<Pixel> pixels = new ArrayList<>();
        private int marker;

        DifferenceRectangle(int marker) {
            this.marker = marker;
        }

        void addPixel(int posX, int posY) {
            this.pixels.add(new Pixel(posX, posY, this.marker));
        }

        Pixel getBoundPixel(boolean leftUpper) {
            HashSet<Integer> posXSet = new HashSet<>();
            HashSet<Integer> posYSet = new HashSet<>();
            for (Pixel currPixel : this.pixels) {
                posXSet.add(currPixel.getCoordinate_X());
                posYSet.add(currPixel.getCoordinate_Y());
            }
            int posX;
            int posY;

            if (leftUpper) {
                posX = Collections.min(posXSet);
                posY = Collections.min(posYSet);
            } else {
                posX = Collections.max(posXSet);
                posY = Collections.max(posYSet);
            }
            return new Pixel(posX, posY, this.marker);
        }

    }

    private class IntegerComparator implements Comparator<Integer> {

        @Override
        public int compare(Integer o1, Integer o2) {
            if (!o1.equals(o2)) {
                int i = Integer.compare(o1, o2);
                int max = Math.max(o1, o2);
                if (i > max / 10) {
                    return i;
                }
            }
            return 0;
        }
    }
}
