package com.mygdx.game.view.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.mygdx.game.PeerHelperInterface;
import com.mygdx.game.WifiDirectInterface;
import com.mygdx.game.view.MyGdxGame;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by antonlin on 2016-05-18.
 */
public class CreateGameDialogueState extends State {

    private Stage stage;
    private BitmapFont font;
    private TextField textInput;

    public CreateGameDialogueState(MyGdxGame game, StateManager stateManager, WifiDirectInterface wifiDirect,
                 PeerHelperInterface peerHelperInterface) {
        super(game,stateManager, StateManager.STATE_NAME.CREATE_GAME_DIALOGUE_STATE, wifiDirect,peerHelperInterface);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("coves_light.otf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 100;
        font = generator.generateFont(parameter); // font size 12 pixels
        generator.dispose();

        stage = new Stage(new ScalingViewport(Scaling.stretch,(float) game.getWidth(), (float)game.getHeight()));

        TextField.TextFieldStyle inputStyle = new TextField.TextFieldStyle();
        inputStyle.font = font;
        inputStyle.fontColor = Color.WHITE;
        textInput = new TextField("",inputStyle);
        textInput.setPosition((int)(game.getWidth()/2), (int)(game.getHeight()/2));

        stage.addActor(textInput);
        setupDialogueButtons();

        setupCustomInputProcessor();
    }

    @Override
    public void update() {
        stage.draw();
    }

    @Override
    public void render(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer) {
//        spriteBatch.begin();
//        Color c = spriteBatch.getColor();
//        spriteBatch.draw(text1, (Gdx.graphics.getWidth() / 2) - (text1.getWidth() / 2),
//                (Gdx.graphics.getHeight() / 4));
//        spriteBatch.draw(button1, (Gdx.graphics.getWidth() / 2) - (button1.getWidth() / 2),
//                (Gdx.graphics.getHeight() / 2) + 50);
//        spriteBatch.draw(button2, (Gdx.graphics.getWidth() / 2) - (button2.getWidth() / 2),
//                (Gdx.graphics.getHeight() / 2) + 70 + (button1.getHeight()));
//        spriteBatch.draw(button3, (Gdx.graphics.getWidth() / 2) - (button3.getWidth() / 2),
//                (Gdx.graphics.getHeight() / 2) + 90 + (button1.getHeight()) + (button2.getHeight()));
//        spriteBatch.end();
    }

    @Override
    public boolean isActive() {
        return (this.stateManager.getActiveStateName().equals(this.stateName));
    }

    public void setupCustomInputProcessor() {
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(game);
        multiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(multiplexer);
    }

    private void setupDialogueButtons() {
        List<TextButton> buttons = new ArrayList<TextButton>();

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;
        TextButton createButton = new TextButton("CREATE", textButtonStyle);
        createButton.setX((float) ((game.getWidth() - createButton.getWidth()) /2 ));
        createButton.setY((float) (createButton.getHeight()));

        buttons.add(createButton);

//        (Gdx.graphics.getWidth() / 2) - (button.getWidth() / 2),
//                Gdx.graphics.getHeight() - button.getHeight() - 100
        for(TextButton button :  buttons){
            stage.addActor(button);
            button.addListener(new ChangeListener() {
                @Override
                public void changed (ChangeEvent event, Actor actor) {
                    System.out.println(/*((TextButton)actor).getText()+*/" button Pressed");
                }
            });
        }
    }
}
