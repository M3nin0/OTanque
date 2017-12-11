package com.app.teste;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

import com.app.ammunition.Missel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class SetSerial {

	public static void main(String[] args) {

		// Set<Missel> misseis = new HashSet<>();
		// Set<Missel> safeMissel = Collections.synchronizedSet(misseis);

		// LinkedList<Missel> misseis = new LinkedList<>();

		String[] argos = new String[2];
		Gson gson = new Gson();
		GsonBuilder builder = new GsonBuilder();
		Gson gsonDois = builder.excludeFieldsWithoutExposeAnnotation().create();

		List<Missel> list2 = new LinkedList<Missel>();
//		list2.add(new Missel(0, 0, 0, null, 0));
//		list2.add(new Missel(120, 021, 23, null, 2));
//		list2.add(new Missel(230, 12, 223, null, 3));

		Type linkedListType2 = new TypeToken<LinkedList<Missel>>() {

		}.getType();

		String json = gson.toJson(list2, linkedListType2);
		//
		// List<String> list = new LinkedList<String>();
		// list.add("a1");
		// list.add("a2");
		// Type linkedListType = new TypeToken<LinkedList<String>>() {
		// }.getType();
		// String json = gson.toJson(list, linkedListType);

		System.out.println(json);

//		Type linkedListType22 = new TypeToken<LinkedList<Missel>>() {
//
//		}.getType();
		List<String> list = gson.fromJson(json, linkedListType2);
		
		System.out.println(list.getClass());

		//
		// misseis.add(new Missel(0, 0, 0, null, 0));
		// misseis.add(new Missel(1, 2, 3, null, 2));
		// misseis.add(new Missel(99, 5, 7, null, 3));
		//
		// String json = gsonDois.toJson(misseis);
		//
		// LinkedList<Missel> nova = gsonDois.fromJson(json, LinkedList.class );
		//
		// System.out.println(nova);

		// System.out.println(nova);

		// System.out.println(safeMissel);
		//
		// String json = gson.toJson(safeMissel, Set.class);
		//
		// safeMissel.removeAll(safeMissel);
		//
		// System.out.println(safeMissel);
		//
		// Set<Missel> novo = gsonDois.fromJson(json, Set.class);
		//
		// String[] tst = new String[5];

		// tst = novo.toString().split(",");

		// System.out.println(novo);

		// misseis.addAll(novo);
		//
		// System.out.println(safeMissel);
		//
		// System.out.println(misseis);

		// for (Missel missel: novo) {
		// System.out.println(missel.getX());
		// }
		//

		// Set<String> bombas = new HashSet<>();
		//
		// List<String> teste = new ArrayList<>();
		//
		// bombas.add("Felipe");
		// bombas.add("POO");
		// bombas.add("Socket");
		//
		// misseis.add(new Missel(0, 0, 0, null, 0));
		// misseis.add(new Missel(1, 2, 3, null, 2));
		//
		// String json = gson.toJson(teste);
		// String jsonDois = gson.toJson(bombas);
		// String jsonTres = gson.toJson(misseis);
		// //
		// // System.out.println(json);
		// // System.out.println(jsonDois);
		// // System.out.println(jsonTres);
		//
		// String stringTeste = jsonDois + ";" + jsonTres;
		// //
		// argos = stringTeste.split(";");
		// //
		// System.out.println(argos[0]);
		// //
		// // // Set<Missel> m2 = new HashSet<>();
		// //
		//
		// //
		// // // m2 = gson.fromJson(jsonTres, misseis.getClass());
		// //
		// // // System.out.println(m2);
		// //
		// // Set<Missel> m3 = new HashSet<>();
		// // m3 = gsonDois.fromJson(jsonTres, misseis.getClass());
		// //
		// // System.out.println(m3);

	}
}
