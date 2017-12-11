package com.app.teste;

import java.awt.Color;

import com.app.characters.Tanque;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Teste {

	private static void serializeUser() {

		// UserSimple user = new UserSimple("Felipe", "Email@emai.cim", 22, true);

		// Gson gson = new Gson();
		// String json = gson.toJson(user);

		// Tanque tanque = new Tanque(0, 0, 0, null, 0);
		//
		// String json = gson.toJson(tanque);
		//
		// System.out.println(json);

		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.excludeFieldsWithoutExposeAnnotation().create();

//		Tanque tanque = new Tanque(400, 50, 180, Color.BLUE, 1);
		
//		String json = gson.toJson(tanque);
		
//		System.out.println(json);

	}

	public static void main(String[] args) {
		serializeUser();
	}

}
