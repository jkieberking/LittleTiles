package com.creativemd.littletiles.common.gui;

import com.cleanroommc.modularui.api.GuiAxis;
import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.GuiTextures;
import com.cleanroommc.modularui.drawable.HueBar;
import com.cleanroommc.modularui.drawable.Rectangle;
import com.cleanroommc.modularui.screen.CustomModularScreen;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.ModularScreen;
import com.cleanroommc.modularui.screen.viewport.ModularGuiContext;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.Color;
import com.cleanroommc.modularui.value.DoubleValue;
import com.cleanroommc.modularui.value.IntValue;
import com.cleanroommc.modularui.value.StringValue;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.*;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.layout.Row;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class GuiChisel extends CustomModularScreen {
    private static final Logger log = LogManager.getLogger(GuiChisel.class);

    private static IDrawable handleBackground = new Rectangle().setColor(Color.WHITE.main);

    private int color = Color.WHITE.main;

    private int alpha;
    private boolean controlAlpha;

    private Rectangle preview = new Rectangle();
    private Rectangle sliderBackgroundR = new Rectangle();
    private Rectangle sliderBackgroundG = new Rectangle();
    private Rectangle sliderBackgroundB = new Rectangle();
    private Rectangle sliderBackgroundA = new Rectangle();
    private Rectangle sliderBackgroundS = new Rectangle();
    private Rectangle sliderBackgroundV = new Rectangle();

    @Override
    public @NotNull ModularPanel buildUI(ModularGuiContext context) {
        this.preview = new Rectangle();
        this.sliderBackgroundR = new Rectangle();
        this.sliderBackgroundG = new Rectangle();
        this.sliderBackgroundB = new Rectangle();
        this.sliderBackgroundA = new Rectangle();
        this.sliderBackgroundS = new Rectangle();
        this.sliderBackgroundV = new Rectangle();
        this.controlAlpha = false;
        PagedWidget.Controller controller = new PagedWidget.Controller();
        Rectangle rectangle = new Rectangle().setColor(Color.WHITE.main);
        this.alpha = rectangle.getColor();

        IWidget alphaSlider = new ParentWidget<>()
            .child(new Row()
                .widthRel(1f).height(12))
                .child(IKey.str("A: ").asWidget()
                    .heightRel(1f)
            )
            .child(createSlider(this.sliderBackgroundA)
                .bounds(0, 255)
                .value(new DoubleValue.Dynamic(() -> Color.getAlpha(this.color), val -> updateColor(Color.withAlpha(this.color, (int) val)))));

        ModularPanel panel = new ModularPanel("littletiles_chisel")
//            panel.flex()                        // returns object which is responsible for sizing
            .size(176, 220)       // set a static size for the main panel
            .align(Alignment.Center);
            panel.child(IKey.str("Chisel Config").asWidget()
                .top(7).left(7))
            .child(new Column()
                .left(5).right(5).top(5).bottom(5)
                .child(new Row()
                    .left(5).right(5).height(14)
                    .child(new PageButton(0, controller)
                        .sizeRel(0.5f, 1f)
                        .invertSelected(true)
                        .overlay(IKey.str("RGB")))
                    .child(new PageButton(1, controller)
                        .sizeRel(0.5f, 1f)
                        .invertSelected(true)
                        .overlay(IKey.str("HSV"))))
            .child(new Row().widthRel(1f).height(12).marginTop(4)
                .child(IKey.str("Hex: ").asWidget().heightRel(1f))
                .child(new TextFieldWidget()
                    .height(12)
                    .expanded()
                    .setValidator(this::validateRawColor)
                    .value(new StringValue.Dynamic(() -> {
                        if (controlAlpha) {
                            return "#" + Integer.toHexString(this.color);
                        }
                        return "#" + Integer.toHexString(Color.withAlpha(this.color, 0));
                    }, val -> {
                        try {
                            updateColor(Integer.decode(val));
                        } catch (NumberFormatException ignored) {
                        }
                    })))
            )
                // @TODO this is broken, investigate why
                // .child(this.preview.asWidget().background(GuiTextures.CHECKBOARD).size(10, 10).margin(1)))
                .child(new PagedWidget<>()
                    .debugName("rgn/hsv picker page parent")
                    .sizeRel(1f)
                    .controller(controller)
                    .addPage(createRGBPage(alphaSlider))
                    .addPage(createHSVPage(alphaSlider))
                )
                .child(new Row()
                    .left(10).right(10).height(14)
                    .mainAxisAlignment(Alignment.MainAxis.SPACE_BETWEEN)
                    .child(new ButtonWidget<>()
                        .heightRel(1f).width(50)
                        .overlay(IKey.str("Cancel"))
                        .onMousePressed(button -> {
// @TODO animate close (needs panel initialization)
                            //                        panel.animateClose();
                            return true;
                        }))
                    .child(new ButtonWidget<>()
                        .heightRel(1f).width(50)
                        .overlay(IKey.str("Confirm"))
                        .onMousePressed(button -> {
                            log.info("closed with " + this.color);
                            return true;
                        }))
                )
            );

        return panel;
    }

    private static SliderWidget createSlider(IDrawable background) {
        return new SliderWidget()
            .expanded()
            .heightRel(1f)
            // .background(background.asIcon().size(0, 4))
            .sliderTexture(handleBackground)
            .sliderSize(2, 8);
    }

    public void updateColor(int color) {
        this.color = color;
        color = Color.withAlpha(color, 255);
        int rs = Color.withRed(color, 0), re = Color.withRed(color, 255);
        int gs = Color.withGreen(color, 0), ge = Color.withGreen(color, 255);
        int bs = Color.withBlue(color, 0), be = Color.withBlue(color, 255);
        int as = Color.withAlpha(color, 0), ae = Color.withAlpha(color, 255);
        this.sliderBackgroundR.setHorizontalGradient(rs, re);
        this.sliderBackgroundG.setHorizontalGradient(gs, ge);
        this.sliderBackgroundB.setHorizontalGradient(bs, be);
        this.sliderBackgroundA.setHorizontalGradient(as, ae);
        this.sliderBackgroundS.setHorizontalGradient(Color.withHSVSaturation(color, 0f), Color.withHSVSaturation(color, 1f));
        this.sliderBackgroundV.setHorizontalGradient(Color.withValue(color, 0f), Color.withValue(color, 1f));
        this.preview.setColor(this.color);
    }

    private String validateRawColor(String raw) {
        if (!raw.startsWith("#")) {
            if (raw.startsWith("0x") || raw.startsWith("0X")) {
                raw = raw.substring(2);
            }
            return "#" + raw;
        }
        return raw;
    }

    private IWidget createRGBPage(IWidget alphaSlider) {
        return new ParentWidget<>()
            .child(new Column()
                .sizeRel(1f, 1f)
                .child(new Row()
                    .widthRel(1f).height(12)
                    .child(IKey.str("R: ").asWidget().heightRel(1f))
                    .child(createSlider(this.sliderBackgroundR)
                        .bounds(0, 255)
                        .value(new DoubleValue.Dynamic(() -> Color.getRed(this.color), val -> updateColor(Color.withRed(this.color, (int) val)))))
                )
                .child(new Row()
                    .widthRel(1f).height(12)
                    .child(IKey.str("G: ").asWidget().heightRel(1f))
                    .child(createSlider(this.sliderBackgroundG)
                        .bounds(0, 255)
                        .value(new DoubleValue.Dynamic(() -> Color.getGreen(this.color), val -> updateColor(Color.withGreen(this.color, (int) val)))))
                )
                .child(new Row()
                    .widthRel(1f).height(12)
                    .child(IKey.str("B: ").asWidget().heightRel(1f))
                    .child(createSlider(this.sliderBackgroundB)
                        .bounds(0, 255)
                        .value(new DoubleValue.Dynamic(() -> Color.getBlue(this.color), val -> updateColor(Color.withBlue(this.color, (int) val)))))
                )
                .childIf(alphaSlider != null, alphaSlider)
            );
    }

    private IWidget createHSVPage(IWidget alphaSlider) {
        return new ParentWidget<>()
            .child(new Column()
                .sizeRel(1f, 1f)
                .child(new Row()
                    .widthRel(1f).height(12)
                    .child(IKey.str("H: ").asWidget().heightRel(1f))
                    .child(createSlider(new HueBar(GuiAxis.X))
                        .bounds(0, 360)
                        .value(new DoubleValue.Dynamic(() -> Color.getHue(this.color), val -> updateColor(Color.withHSVHue(this.color, (float) val))))))
                .child(new Row()
                    .widthRel(1f).height(12)
                    .child(IKey.str("S: ").asWidget().heightRel(1f))
                    .child(createSlider(this.sliderBackgroundS)
                        .bounds(0, 1)
                        .value(new DoubleValue.Dynamic(() -> Color.getHSVSaturation(this.color), val -> updateColor(Color.withHSVSaturation(this.color, (float) val))))))
                .child(new Row()
                    .widthRel(1f).height(12)
                    .child(IKey.str("V: ").asWidget().heightRel(1f))
                    .child(createSlider(this.sliderBackgroundV)
                        .bounds(0, 1)
                        .value(new DoubleValue.Dynamic(() -> Color.getValue(this.color), val -> updateColor(Color.withValue(this.color, (float) val))))))
                .childIf(alphaSlider != null, alphaSlider)
            );
    }
}
