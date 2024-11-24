package hbe.fitness_zone;

import com.formdev.flatlaf.FlatDarculaLaf;
import hbe.fitness_zone.gui.FitnessZoneForm;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import javax.swing.*;

@SpringBootApplication
public class FitnessZoneSwing {
    public static void main(String[] args) {
        // Dark mode config
        FlatDarculaLaf.setup();

        // Instantiate spring factory
        ConfigurableApplicationContext contextSpring =
                new SpringApplicationBuilder(FitnessZoneSwing.class)
                        .headless(false)
                        .web(WebApplicationType.NONE).
                        run(args);

        // Create a Swing object
        SwingUtilities.invokeLater(() -> {
            FitnessZoneForm fitnessZoneForm = contextSpring.getBean(FitnessZoneForm.class);
            fitnessZoneForm.setVisible(true);
        });
    }
}
