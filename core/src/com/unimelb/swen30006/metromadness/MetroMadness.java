package com.unimelb.swen30006.metromadness;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;

/** SWEN30006 Software Modeling and Design
MetroMadness class
George Juliff - 624946
David Murges - 657384
Thomas Miles - 626263

Overarching class that sets up the simulation and handles interactions with libGDX
*/
public class MetroMadness extends ApplicationAdapter {
	
	// Name of map Files
	private static final String filename = "../core/assets/maps/melbourne.xml";
//	private static final String filename = "../core/assets/maps/world.xml";

	// The width of the world in unitless dimensions
    static final int WORLD_WIDTH = 1200;
    static final int WORLD_HEIGHT = 1200;

    // Viewport state
    private int VIEWPORT_WIDTH = WORLD_WIDTH;
	private float viewport_width;

	// Data for simluation, rendering and camera.
	Simulation sim;
	ShapeRenderer shapeRenderer;
	OrthographicCamera camera;
	
	// Font
	BitmapFont smaller;
	BitmapFont header;

	/**
	 * Override to set the size of the map
	 */
	@Override
	public void resize(int width, int height) {
        camera.viewportWidth = viewport_width;
        camera.viewportHeight = viewport_width * (float)height/width;
        camera.update();
	}
	
	/**
	 * Override to set up the simulation run the simulation
	 */
	@Override
	public void create () {
		// Create the simulation
		sim = new Simulation(filename);		
		
		// Setup our 2D Camera
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        viewport_width = VIEWPORT_WIDTH;
		camera = new OrthographicCamera(viewport_width, viewport_width * (h / w));
		camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
		camera.update();
		
		// Create our shape renderer
		shapeRenderer = new ShapeRenderer();
		
		// Create our font
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("assets/fonts/Gotham-Book.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = 12;
		smaller = generator.generateFont(parameter); // font size 12 pixels
		generator.dispose(); // don't forget to dispose to avoid memory leaks!

		FreeTypeFontGenerator headlineGen = new FreeTypeFontGenerator(Gdx.files.internal("assets/fonts/Gotham-Bold.ttf"));
		FreeTypeFontParameter headlineParam = new FreeTypeFontParameter();
		headlineParam.size = 40;
		header = headlineGen.generateFont(headlineParam); // font size 12 pixels
		headlineGen.dispose(); // don't forget to dispose to avoid memory leaks!
		
		// Setup fonts
		 smaller.setColor(Color.GRAY);
		 header.setColor(Color.BLACK);

	}

	/**
	 * Override to render all aspects of the simulation, and the title
	 */
	@Override
	public void render () {
		// Clear the graphics to white
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		// Handle user input
		handleInput();
		
		// Update the simulation and camera
		camera.update();
		sim.update();
		
		// Render the simulation
		 shapeRenderer.setProjectionMatrix(camera.combined);
		 
		 // Render all filled shapes.
		 shapeRenderer.begin(ShapeType.Filled);
		 sim.render(shapeRenderer);
		 shapeRenderer.end();
		 
		 // Begin preparations to render text
		 SpriteBatch batch = new SpriteBatch();
		 batch.begin();

		 // Render Header
		 header.getData().setScale(0.5f);
		 header.draw(batch, "metro madness.", 10, Gdx.graphics.getHeight()-10);
		 batch.end();

	}
	
	/**
	 * private method to simplify the render method by handling user input
	 */
    private void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            camera.zoom += 0.1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
        	camera.zoom -= 0.1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)){
        	camera.translate(-3f, 0, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.E)) {
            Gdx.app.exit();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
        	camera.translate(3f, 0, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
        	camera.translate(0, -3f, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
        	camera.translate(0, 3f, 0);
        }

        
        camera.zoom = MathUtils.clamp(camera.zoom, 0.1f, WORLD_WIDTH/camera.viewportWidth);
        float effectiveViewportWidth = camera.viewportWidth * camera.zoom;
        float effectiveViewportHeight = camera.viewportHeight * camera.zoom;

        camera.position.x = MathUtils.clamp(camera.position.x, effectiveViewportWidth / 2f, WORLD_WIDTH - effectiveViewportWidth / 2f);
        camera.position.y = MathUtils.clamp(camera.position.y, effectiveViewportHeight / 2f, WORLD_HEIGHT - effectiveViewportHeight / 2f);
    }

}
