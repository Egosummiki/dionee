package com.square.game;

import com.badlogic.gdx.Gdx;

/**
 * Created by Mikolaj on 07.11.2015.
 */
public class GuiSettings extends Gui {

    private int egg_counter = 0;

    public GuiSettings()
    {
        int sx = (Gdx.graphics.getWidth()/2)-176;
        int sy = (Gdx.graphics.getHeight()/2)-144;

        addElement(new GuiImage(Render.TEXTURE_GUI_LEVELMENU_BACK, sx, sy, 352, 288));

        addElement(new Button(Render.TEXTURE_GOBACK, 16, Gdx.graphics.getHeight() - 80, 64, 64).setAction(new ButtonAction() {
            @Override
            public void onPress(Button but, float time) {

            }

            @Override
            public void onTap(Button but, float time) {

            }

            @Override
            public void onTapRelease(Button but, float time) {
                elements.get(1).setAnimation(GuiElement.AnimationType.SLIDE_TO_LEFT, 300);
                setAnimationExcluding(GuiElement.AnimationType.SLIDE_TO_BOTTOM, 300, new int[]{1}, new GuiElement.AnimationCallback() {
                    @Override
                    public void onAnimationEnd() {
                        Gui.setGui(Gui.GUI_MENU);
                    }
                });
            }
        }));

        addElement(new Slidebar(sx + 72, sy + 192, 0));

        addElement(new Button(Render.TEXTURE_MUSIC_NOTE, sx + 32, sy + 192, 32, 32).setAction(new ButtonAction() {
            @Override
            public void onPress(Button but, float time) {

            }

            @Override
            public void onTap(Button but, float time) {
                egg_counter++;

                if(egg_counter >= 13)
                {
                    egg_counter = 0;
                    GameMusic.setCurrentSong(GameMusic.Song.easter_egg);
                }
            }

            @Override
            public void onTapRelease(Button but, float time) {

            }
        }));
    }

    @Override
    public void update(float time)
    {
        super.update(time);

        Slidebar bar = (Slidebar)elements.get(2);

        if(bar.processChanged())
        {
            if(bar.getProcessFloat() < 0.15f)
            {
                GameMusic.setMusicLoudness(0.0f);
                bar.setProcessFloat(0);
                ((Button)elements.get(3)).setTextureNormal(Render.TEXTURE_MUSIC_NOTE_NO);
                ((Button)elements.get(3)).setTexturePressed(Render.TEXTURE_MUSIC_NOTE_NO);
                ((Button)elements.get(3)).setTexture(Render.TEXTURE_MUSIC_NOTE_NO);
            } else
            {
                GameMusic.setMusicLoudness(bar.getProcessFloat());
                ((Button)elements.get(3)).setTextureNormal(Render.TEXTURE_MUSIC_NOTE);
                ((Button)elements.get(3)).setTexturePressed(Render.TEXTURE_MUSIC_NOTE);
                ((Button)elements.get(3)).setTexture(Render.TEXTURE_MUSIC_NOTE);
            }
        }

        if(bar.released())
        {
            SaveData.setMusicVol(bar.getProcessFloat());
        }
    }

    @Override
    public void onSet()
    {
        egg_counter = 0;

        resetAll();
        setAnimationExcluding(GuiElement.AnimationType.SLIDE_FROM_BOTTOM, 300, new int[]{1});
        elements.get(1).setAnimation(GuiElement.AnimationType.SLIDE_FROM_LEFT, 300);

        ((Slidebar)elements.get(2)).setProcessFloat(SaveData.getMusicVol());

        if(SaveData.getMusicVol() == 0)
        {
            ((Button)elements.get(3)).setTextureNormal(Render.TEXTURE_MUSIC_NOTE_NO);
            ((Button)elements.get(3)).setTexturePressed(Render.TEXTURE_MUSIC_NOTE_NO);
            ((Button)elements.get(3)).setTexture(Render.TEXTURE_MUSIC_NOTE_NO);
        }
    }



}
