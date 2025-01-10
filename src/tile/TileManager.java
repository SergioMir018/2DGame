package tile;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Objects;

public class TileManager {
    GamePanel gp;
    Tile[] tiles;
    int[][] mapTileMatrix;

    public TileManager(GamePanel gp) {
        this.gp = gp;
        tiles = new Tile[10];
        mapTileMatrix = new int[gp.maxScreenColumn][gp.maxScreenRow];

        getTileImage();
        loadMapMatrix("/maps/map01.txt");
    }

    public void getTileImage() {
        try {
            tiles[0] = new Tile();
            tiles[0].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/grass00.png")));

            tiles[1] = new Tile();
            tiles[1].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/wall.png")));

            tiles[2] = new Tile();
            tiles[2].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/water00.png")));

        } catch (IOException | NullPointerException e) {
            throw new RuntimeException("Error on loading tiles sprites: " + e.getMessage());
        }
    }

    public void loadMapMatrix(String mapFilePath) {
        try {
            InputStream inputStream = Objects.requireNonNull(getClass().getResourceAsStream(mapFilePath));
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            int row = 0;

            while (row < gp.maxScreenRow) {
                int[] line = Arrays.stream(bufferedReader.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
                mapTileMatrix[row] = line;
                row++;
            }

            bufferedReader.close();
        } catch (Exception e) {
            throw new RuntimeException("Error on loading map matrix data: " + e.getMessage());
        }
    }

    public void draw(Graphics2D g2) {
        int col = 0;
        int row = 0;
        int x = 0;
        int y = 0;

        while (col < gp.maxScreenColumn && row < gp.maxScreenRow) {
            int tileNum = mapTileMatrix[row][col];
            g2.drawImage(tiles[tileNum].image, x, y, gp.tileSize, gp.tileSize, null);
            col++;
            x += gp.tileSize;

            if (col == gp.maxScreenColumn) {
                col = 0;
                x = 0;
                row++;
                y += gp.tileSize;
            }
        }
    }
}
