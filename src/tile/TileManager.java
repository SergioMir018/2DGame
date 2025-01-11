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
    public Tile[] tiles;
    public int[][] mapTileMatrix;

    public TileManager(GamePanel gp) {
        this.gp = gp;
        tiles = new Tile[10];
        mapTileMatrix = new int[gp.maxWorldCol][gp.maxWorldRow];

        getTileImage();
        loadMapMatrix("/maps/world01.txt");
    }

    public void getTileImage() {
        try {
            tiles[0] = new Tile();
            tiles[0].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/grass00.png")));

            tiles[1] = new Tile();
            tiles[1].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/wall.png")));
            tiles[1].collision = true;

            tiles[2] = new Tile();
            tiles[2].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/water00.png")));
            tiles[2].collision = true;

            tiles[3] = new Tile();
            tiles[3].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/earth.png")));

            tiles[4] = new Tile();
            tiles[4].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/tree.png")));
            tiles[4].collision = true;

            tiles[5] = new Tile();
            tiles[5].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/road00.png")));

        } catch (IOException | NullPointerException e) {
            throw new RuntimeException("Error on loading tiles sprites: " + e.getMessage());
        }
    }

    public void loadMapMatrix(String mapFilePath) {
        try {
            InputStream inputStream = Objects.requireNonNull(getClass().getResourceAsStream(mapFilePath));
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            int row = 0;

            while (row < gp.maxWorldCol) {
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
        int worldCol = 0;
        int worldRow = 0;

        while (worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow) {
            int tileNum = mapTileMatrix[worldRow][worldCol];
            int worldX = worldCol * gp.tileSize;
            int worldY = worldRow * gp.tileSize;
            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            int screenY = worldY - gp.player.worldY + gp.player.screenY;

            if (
                    worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                    worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                    worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                    worldY - gp.tileSize < gp.player.worldY + gp.player.screenY
            ) {
                g2.drawImage(tiles[tileNum].image, screenX, screenY, gp.tileSize, gp.tileSize, null);
                if (tiles[tileNum].collision) {
                    g2.setColor(Color.RED); // Establece el color del rectángulo (puedes cambiarlo)
                    g2.drawRect(
                            screenX,
                            screenY,
                            gp.tileSize,
                            gp.tileSize
                    );
                }
            }

            worldCol++;
            if (worldCol == gp.maxWorldCol) {
                worldCol = 0;
                worldRow++;
            }
        }
    }

}
