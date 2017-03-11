package Loaders;

import Objects.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Created by HP PC on 25.02.2017.
 */
public class LevelLoader {

    private int width;
    private int height;

    private BufferedImage[] backgroundImages;

    private List<GameObject> objects;
    private HashMap<String, Texture> textures = new HashMap<>();
    private HashMap<String, BufferedImage[][]> chineseTextures = new HashMap<>();
    private HashMap<String, BufferedImage[][]> playerTextures = new HashMap<>();
    private Player player;

    public LevelLoader(int width, int height, String[] backgroundPath, String[] textures){
        this.width = width;
        this.height = height;

        backgroundImages = new BufferedImage[backgroundPath.length];

        backgroundImages = ImageLoader.loadImages(backgroundPath);
        loadTextures(textures);
        loadCharactersTextures();
    }

    private void loadTextures(String[] path){
        for(int i = 0; i < path.length; i++){
            File file = new File(path[i]);

            String name = nameConverter(file.getName());
            Texture texture = textureFabric(name, path[i]);

            textures.put(name, texture);
        }
    }

    private void loadCharactersTextures(){
        String pl = "player/";
        Texture playerTextureStandingRight = new Texture(48, 105, pl + "standing.png");
        playerTextures.put("standing", playerTextureStandingRight.getTextures());
        playerTextures.put("walking", new Texture(42, 105, pl +"walking.png").getTextures());
        playerTextures.put("kick", new Texture(60, 105, pl +"kick.png").getTextures());
        playerTextures.put("kick_2", new Texture(57, 105, pl +"kick_2.png").getTextures());
        playerTextures.put("leg_kick", new Texture(57, 105, pl +"leg_kick.png").getTextures());
        playerTextures.put("kicked_final_1", new Texture(47, 105, pl +"kicked_final_1.png").getTextures());
        playerTextures.put("kicked_final_2", new Texture(47, 105, pl +"kicked_final_2.png").getTextures());
        playerTextures.put("kicked_first",new Texture(56, 105, pl +"kicked_first.png").getTextures());
        playerTextures.put("kicked_second", new Texture(57, 105, pl +"kicked_second.png").getTextures());
        playerTextures.put("died_up", new Texture(72, 105, pl +"died_up.png").getTextures());
        playerTextures.put("died_down", new Texture(83, 105, pl +"died_down.png").getTextures());

        String ch = "chinese/";
        Texture chineseTextureStandingRight = new Texture(48, 105, ch + "standing.png");
        chineseTextures.put("standing", chineseTextureStandingRight.getTextures());
        chineseTextures.put("walking", new Texture(50, 99, ch +"walking.png").getTextures());
        chineseTextures.put("kick", new Texture(59, 105, ch +"kick.png").getTextures());
        chineseTextures.put("kick_second", new Texture(55, 105, ch +"kick_second.png").getTextures());
        chineseTextures.put("leg_kick", new Texture(64, 105, ch +"leg_kick.png").getTextures());
        chineseTextures.put("kicked_first", new Texture(50, 105, ch +"kicked_first.png").getTextures());
        chineseTextures.put("kicked_second", new Texture(50, 105, ch +"kicked_second.png").getTextures());
        chineseTextures.put("kicked_final", new Texture(47, 102, ch +"kicked_final_standing.png").getTextures());
        chineseTextures.put("died_down", new Texture(88, 105, ch +"died_down.png").getTextures());
        chineseTextures.put("died_up", new Texture(61, 105, ch +"died_up.png").getTextures());
    }

    private Texture textureFabric(String name, String path){
        Texture texture = null;
        switch (name){
            case "stone":{
                texture = new Texture(400,320, path);
                break;
            }
        }
        return texture;
    }

    private String nameConverter(String fileName){
        String name = fileName;
        int index = name.lastIndexOf("\\");
        name = name.substring(index+1);

        index = name.indexOf(".");
        name = name.substring(0, index);

        return name;
    }

    public void loadLevel(String mapPath){
        BufferedImage levelImage = ImageLoader.loadImage(mapPath);

        for(int x = 0; x < levelImage.getWidth(); x++){
            for(int y = 0; y < levelImage.getHeight(); y++){
                int pix = levelImage.getRGB(x, y);

                int red = (pix >> 16) & 0xff;
                int green = (pix >> 8) & 0xff;
                int blue = pix & 0xff;

                if((red == 255) && (green == 255) && (blue == 255)){
                    objects.add(new Block(x*32 ,y*32, 32, 32, GameObjectId.Block, textures.get("stone").getTextures()[0][0]));
                }
                if((red == 0) && (green == 0) && (blue == 255)){
                    player = new Player(x, y, 48,  105, objects, GameObjectId.Player, playerTextures.get("standing")[0][0], playerTextures);
                }
                if((red == 255) && (green == 0) && (blue == 0)){
                    Enemy enemy = new Chinese(x, y, 48,  105, objects, GameObjectId.Enemy, chineseTextures.get("standing")[0][0], chineseTextures);
                    enemy.setPosition(new Random().nextBoolean());
                    objects.add(enemy);
                }
            }
        }
        objects.add(player);
    }

    public List<GameObject> loadLevel(){
        objects = new ArrayList<>();
        for(int x = 0; x < 25; x++){
            objects.add(new Block(x*32 ,13*32, 32, 32, GameObjectId.Block, textures.get("stone").getTextures()[0][0]));
        }

        Random random = new Random();

        for(int i = 0; i < random.nextInt(2) + 2; i++){
            objects.add(new Chinese(300 + random.nextInt(width/3) + i*100, 200, 48,  105, objects, GameObjectId.Enemy, chineseTextures.get("standing")[0][0], chineseTextures));
        }

        player = new Player(100, 200, 48,  105, objects, GameObjectId.Player, playerTextures.get("standing")[0][0], playerTextures);
        objects.add(player);
        return objects;
    }

    public Block getLastBlock(){
        Block block = null;
        int x = 0;
        for(GameObject object : objects){
            if(object.getId() == GameObjectId.Block){
                Block tmp = (Block) object;
                if(tmp.getX() >= x){
                    x = (int) tmp.getX();
                    block = tmp;
                }
            }
        }
        return block;
    }

    public Block getFirstBlock(){
        Block block = null;
        int x =(int) objects.get(0).getX();
        for(GameObject object : objects){
            if(object.getId() == GameObjectId.Block){
                Block tmp = (Block) object;
                if(tmp.getX() <= x){
                    x = (int) tmp.getX();
                    block = tmp;
                }
            }
        }
        return block;
    }

    public BufferedImage[] getBackground(){
        return backgroundImages;
    }

    public List<GameObject> getObjects(){
        return objects;
    }

    public Player getPlayer(){
        return player;
    }

}

