package GUI;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;

import javafx.scene.control.TextField;

@Retention(RUNTIME)
public @interface checkUser {

	public String email() default "";
	
}
