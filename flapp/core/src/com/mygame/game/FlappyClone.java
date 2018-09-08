package com.mygame.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class FlappyClone extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
//	ShapeRenderer shapeRenderer;

	Texture[] birds ;

	private float velocity=0;
	Circle birdcircle;

	private int gamestate=0;
	private int score=0;
	private int scoringtube=0;
	private int birdY=0;
	private int flapstate=0;
	private float gravity = (float) 1.8;
	Random randomgenerator;
	Texture topTube;
	Texture BottomTube;
	Texture GameOver;
	float gap=510;
	float maxtubeoffset;
	float tubevelocity=4;
	int nooftubes=4;
    float[] tubex=new float[nooftubes];
    float[] tubeoffset=new float[nooftubes];
	float distancebetweentubes;
	BitmapFont font;
	Rectangle[] toptuberectangles;
	Rectangle[] bottomtuberectangles;
	@Override
	public void create () {
		batch = new SpriteBatch();
		background=new Texture("bg.png");
		GameOver=new Texture("gameover.png");
//		shapeRenderer=new ShapeRenderer();
		birdcircle=new Circle();
       font =new BitmapFont();
       font.setColor(Color.WHITE);
       font.getData().setScale(10);
		birds=new Texture[2];
		birds[0]=new Texture("bird.png");
		birds[1]=new Texture("bird2.png");

		topTube=new Texture("toptube.png");
		BottomTube=new Texture("bottomtube.png");
		maxtubeoffset=Gdx.graphics.getHeight()/2-gap/2-100;
		randomgenerator=new Random();
		distancebetweentubes=Gdx.graphics.getWidth()*3/4;
		toptuberectangles=new Rectangle[nooftubes];
		bottomtuberectangles=new Rectangle[nooftubes];
        startgame();

	}

public void startgame(){
	birdY=Gdx.graphics.getHeight()/2-birds[0].getHeight()/2;
	for (int i = 0; i <nooftubes ; i++) {
		tubeoffset[i]=(randomgenerator.nextFloat()- 0.5f)*(Gdx.graphics.getHeight()-gap-200);
		tubex[i]=Gdx.graphics.getWidth()/2-BottomTube.getWidth()/2+Gdx.graphics.getWidth()+i*distancebetweentubes;
		toptuberectangles[i]=new Rectangle();
		bottomtuberectangles[i]=new Rectangle();
	}
}

	@Override
	public void render () {
		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());



		if(gamestate==1) {

            if(tubex[scoringtube]< Gdx.graphics.getWidth()/2){
                score++;

            if(scoringtube<nooftubes-1){
                scoringtube++;
            }
            else {
                scoringtube = 0;
            }
            }


			if(Gdx.input.justTouched()){

				velocity=-30;

			}
            for (int i = 0; i <nooftubes ; i++) {

                if(tubex[i] < -topTube.getWidth()){

                    tubex[i]+=nooftubes*distancebetweentubes;
					tubeoffset[i]=(randomgenerator.nextFloat()- 0.5f)*(Gdx.graphics.getHeight()-gap-200);
                }
                else {

                    tubex[i] = tubex[i] - tubevelocity;

                }
                batch.draw(topTube,tubex[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeoffset[i]);
                batch.draw(BottomTube, tubex[i], Gdx.graphics.getHeight() / 2 - gap / 2 - BottomTube.getHeight() + tubeoffset[i]);

                toptuberectangles[i]=new Rectangle(tubex[i],Gdx.graphics.getHeight()/2 +gap/2+tubeoffset[i],topTube.getWidth(),topTube.getHeight());
                bottomtuberectangles[i]=new Rectangle(tubex[i],Gdx.graphics.getHeight() / 2 - gap / 2 - BottomTube.getHeight() + tubeoffset[i],BottomTube.getWidth(),BottomTube.getHeight());
			}

                     if(birdY>0 ){
						 velocity=velocity+gravity;
						 birdY -= velocity;
					 }
					 else {
                          gamestate=2;
					 }




		}
		else if (gamestate==0) {
			if (Gdx.input.justTouched()) {

				gamestate = 1;
			}
		}
			else {
				batch.draw(GameOver,Gdx.graphics.getWidth()/2-GameOver.getWidth()/2,Gdx.graphics.getHeight()/2-GameOver.getHeight()/2);
				if (Gdx.input.justTouched()){

					gamestate=1;
					startgame();
					score=0;
					scoringtube=0;
					velocity=0;
				}

			}

		if (flapstate == 0) {
			flapstate = 1;
		} else {
			flapstate = 0;
		}

		batch.draw(birds[flapstate], Gdx.graphics.getWidth() / 2 - birds[flapstate].getWidth()/2, birdY);
		font.draw(batch,String.valueOf(score),100,100);
		birdcircle.set(Gdx.graphics.getWidth()/2,birdY+birds[flapstate].getHeight()/2,birds[flapstate].getWidth()/2);
//		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//		shapeRenderer.setColor(Color.RED);
//		shapeRenderer.circle(birdcircle.x,birdcircle.y,birdcircle.radius);
		for (int i = 0; i <nooftubes ; i++) {
			//shapeRenderer.rect(tubex[i],Gdx.graphics.getHeight()/2 +gap/2+tubeoffset[i],topTube.getWidth(),topTube.getHeight());
		    //shapeRenderer.rect(tubex[i],Gdx.graphics.getHeight() / 2 - gap / 2 - BottomTube.getHeight() + tubeoffset[i],BottomTube.getWidth(),BottomTube.getHeight());
		    if(Intersector.overlaps(birdcircle,toptuberectangles[i])||Intersector.overlaps(birdcircle,bottomtuberectangles[i])){

		    	gamestate=2;
			}
		}
//		shapeRenderer.end();
        batch.end();
	}

	

}
