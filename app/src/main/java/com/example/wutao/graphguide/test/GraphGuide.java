package com.example.wutao.graphguide.test;


import com.example.wutao.graphguide.data_struct.TrainGraph;
import com.example.wutao.graphguide.utils.Utils;

import java.util.ArrayList;

public class GraphGuide {
	
	public static final String sEdgeInputParams = "AB5, BC4, CD8, DC8, DE6, AD5, CE2, EB3, AE7";

	public static final String sStopLocationInputParams = "A(2:1), B(1:2), C(1:4), D(3:5), E(5:3)";
	
	public static final String NO_SUCH_ROUTE = "NO_SUCH_ROUTE";
	
	private static StringBuilder sRouteSB = new StringBuilder();
	
	public static void main(String[] args) {
		
		TrainGraph graph = TrainGraph.createGraph(sEdgeInputParams, sStopLocationInputParams);
		printDistanceOfRoute(graph, "ABC");
		printDistanceOfRoute(graph, "ABC");
		printDistanceOfRoute(graph, "AD");
		printDistanceOfRoute(graph, "ADC");
		printDistanceOfRoute(graph, "AEBCD");
		printDistanceOfRoute(graph, "AED");
		ArrayList<String> tripsC2C = graph.tripsOfRoute('C', 'C', 3);
		System.out.println("route = " +  (tripsC2C == null ? 0 : tripsC2C.size()));
		ArrayList<String> tripsA2C = graph.tripsOfRoute('A', 'C', 4);
		int count = 0;
		if(!Utils.isEmpty(tripsA2C)){
			for (String trip : tripsA2C) {
				if(!Utils.isEmpty(trip) && trip.length() == 5){
					count++;
				}
			}
		}
		System.out.println("Output #7: " + count);
		System.out.println("Output #8: " + graph.distanceOf(graph.shortestRouteOf('A', 'C')));
	}
	
	private static String distanceString(TrainGraph graph , String route){
		if(graph != null && !Utils.isEmpty(route)){
			int distance = graph.distanceOf(route);
			return distance >= 0 ? String.valueOf(distance) : NO_SUCH_ROUTE;
		}
		return NO_SUCH_ROUTE;
	}
	
	public static String theDistanceOfRoute(TrainGraph graph, String route) {
		if(!Utils.isEmpty(route)){
			sRouteSB.setLength(0);
			sRouteSB.append("The distance of the route ");
			for(int i = 0; i < route.length(); i++){
				sRouteSB.append(route.charAt(i));
				if(i != route.length() - 1){
					sRouteSB.append("-");
				}
			}
			sRouteSB.append(": ");
			sRouteSB.append(distanceString(graph, route));
			return sRouteSB.toString();
		}
		return "";
	}
	
	private static void printDistanceOfRoute(TrainGraph graph, String route){
		System.out.println(theDistanceOfRoute(graph, route));
	}

}
