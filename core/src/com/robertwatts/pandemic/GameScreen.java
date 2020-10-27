package com.robertwatts.pandemic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameScreen extends ScreenAdapter {
    PandemicGame game;
    private int playAreaHeight = Gdx.graphics.getHeight();
    private int playAreaWidth = Gdx.graphics.getWidth();

    private static int characterSize = 50;
    private static float characterSpeed = 8;
    private static float playerSpeedMultiplier = 1;
    private static float enemySpeedMultiplier = (float) 2;

    List<float[]> enemyLocations = new ArrayList<float[]>();

    float playerX = (playAreaWidth/2) - (characterSize/2);
    float playerY = (playAreaHeight/2) - (characterSize/2);

    float foodX;
    float foodY;

    private static Random random = new Random();

    public GameScreen(PandemicGame game){
        this.game = game;
    }

    @Override
    public void show(){
        moveFood();
        newEnemy();
    }

    @Override
    public void render(float delta){

        Gdx.gl.glClearColor(0, .25f, .25f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        keyPress();
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        renderFood();
        renderPlayer();
        renderEnemy();
        game.shapeRenderer.end();

        if(outsideArea(playerX, playerY)){
            game.setScreen(new TitleScreen(game));
        }
        if(isOverlapping(playerX,playerY,foodX,foodY)){
            moveFood();
            newEnemy();
        }
        for (int i = 0; i < enemyLocations.size(); i++) {
            if(isOverlapping(playerX,playerY,enemyLocations.get(i)[0], enemyLocations.get(i)[1])){
                game.setScreen(new TitleScreen(game));
            }
        }





    }

    @Override
    public void hide(){

    }


    private void keyPress(){
        float playerSpeed = characterSpeed * playerSpeedMultiplier;
        if(Gdx.input.isKeyPressed(Input.Keys.W)){
            playerY += playerSpeed;
        } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            playerY -= playerSpeed;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            playerX-= playerSpeed;
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            playerX += playerSpeed;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new TitleScreen(game));
        }

    }

    private void renderPlayer(){
        game.shapeRenderer.setColor(0, 1, 0, 1);
        game.shapeRenderer.rect(playerX, playerY, characterSize ,characterSize);
    }

    private void renderEnemy(){
        float enemySpeed = (characterSpeed * enemySpeedMultiplier);
        int directionalSpeed = (int) (enemySpeed / 2);
        for (int i = 0; i < enemyLocations.size(); i++) {
            int xChange = random.nextInt((directionalSpeed - (-directionalSpeed) + 1)) - directionalSpeed;
            int yChange = random.nextInt((directionalSpeed - (-directionalSpeed) + 1)) - directionalSpeed;

            if (enemyLocations.get(i)[0] <= characterSize - 1) {
                xChange = directionalSpeed;
            } else if (enemyLocations.get(i)[0] >= playAreaWidth - characterSize - 1){
                xChange = -directionalSpeed;
            }
            if (enemyLocations.get(i)[1] <= 1) {
                yChange = directionalSpeed;
            } else if (enemyLocations.get(i)[1] >= playAreaHeight - 1){
                yChange = - directionalSpeed;
            }

            enemyLocations.get(i)[0] += xChange;
            enemyLocations.get(i)[1] += yChange;
            game.shapeRenderer.setColor(0, 1, 1, 1);
            game.shapeRenderer.rect(enemyLocations.get(i)[0], enemyLocations.get(i)[1], characterSize ,characterSize);
        }
    }

    private void renderFood(){
        game.shapeRenderer.setColor(1, 1, 0, 1);
        game.shapeRenderer.rect(foodX, foodY, characterSize ,characterSize);
    }

    private boolean outsideArea(float x, float y) {
        return y < 0 || y > playAreaHeight - characterSize || x < 0 || x > playAreaWidth - characterSize;
    }

    private boolean isOverlapping(float object1X, float object1Y, float object2X, float object2Y){
        return object1X < object2X + characterSize && object1X + characterSize > object2X && object1Y < object2Y + characterSize && object1Y + characterSize > object2Y;
    }

    private void moveFood(){
        foodX = random.nextInt(playAreaWidth - characterSize + 1)+characterSize;
        foodY = random.nextInt(playAreaHeight - characterSize + 1)+characterSize;
    }

    private void newEnemy(){


        float x = (float) (0.5 * characterSize);
        float y = (float) (0.5 * characterSize);

        if (playerX < playAreaWidth / 2){
            x = (float) (playAreaWidth - (1.5 * characterSize));
        }
        if (playerY < playAreaWidth / 2){
            y = (float) (playAreaHeight - (1.5 * characterSize));
        }




        enemyLocations.add(new float[]{x, y});
    }

}
