package com.example.wutao.graphguide.data_struct;

import com.example.wutao.graphguide.utils.Utils;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class TrainGraph {

	static final int INF_DISTANCE = Integer.MAX_VALUE;

	private ArrayList<Stop> mStopsList = new ArrayList<>();
	
	private StringBuilder sBuilder = new StringBuilder();

	public static final int INVALID_TRIP = -1;
	
	private TrainGraph() {
		
	}

	public void addStop(char stopName, int x,  int y){
		Stop newStop = new Stop(stopName, x, y);
		if(!mStopsList.contains(newStop)){
			mStopsList.add(newStop);
		}
	}
	
	public void addTrack(char source, char end, int distance){
		if(indextOf(source) == -1 || indextOf(end) == -1){
			return;
		}
		int endIndex = indextOf(end);
		Stop sourceStop = mStopsList.get(indextOf(source));
		Track next = sourceStop.getFirstTrack();
		while (next != null && next.getAdjStop() != endIndex) {
			next = next.getNext();
		}
		if(next != null){
			//already exist, update
			next.setDistance(distance);
		}else {
			//add a new track
			Track nextTrack = sourceStop.getFirstTrack();
			Track newTrack = new Track();
			sourceStop.setFirstTrack(newTrack);
			newTrack.setAdjStop(endIndex);
			newTrack.setDistance(distance);
			newTrack.setNext(nextTrack);
		}
	}
	
	private int indextOf(char stopName){
		for(int i = 0; i < mStopsList.size(); i++){
			Stop stop = mStopsList.get(i);
			if(stop.getStopName() == stopName){
				return i;
			}
		}
		return -1;
	}
	
	public static TrainGraph createGraph(String edgeInput, String stopLocationInput){
		if(!Utils.isEmpty(edgeInput) && !Utils.isEmpty(stopLocationInput)
				&& Pattern.matches("^([a-zA-Z]{2}\\d, )*([a-zA-Z]{2}\\d)$", edgeInput)
				&& Pattern.matches("^([a-zA-Z]\\(\\d:\\d\\), )*([a-zA-Z]\\(\\d:\\d\\))$", stopLocationInput)){
			String[] splitStopInput = stopLocationInput.split(", ");
			TrainGraph graph = null;
			if(!Utils.isEmpty(splitStopInput)){
				graph = new TrainGraph();
				Pattern intPattern = Pattern.compile("(\\d+)");
				for (String stopInfo : splitStopInput){
					char stopName = stopInfo.charAt(0);
					Matcher intMatcher = intPattern.matcher(stopInfo);
					int x = 0;
					int y = 0;
					for (int i = 0; i < 2 && intMatcher.find(); i++){
						String find = intMatcher.group(1);
						if(i == 0){
							x = Integer.parseInt(find);
						}else {
							y = Integer.parseInt(find);
						}
					}
					graph.addStop(stopName, x, y);
				}
			}
			String[] splitEdgeInput = edgeInput.split(", ");
			if(graph != null && !Utils.isEmpty(splitEdgeInput)){
				for(String element : splitEdgeInput){
					String s = element.trim().toUpperCase();
					char node0 = s.charAt(0);
					char node1 = s.charAt(1);
					int distance = Integer.parseInt(s.substring(s.length() - 1));
					graph.addTrack(node0, node1, distance);
				}
				return graph;
			}
		}
		return null;
	}
	
	public int distanceOf(char... stops) {
		int distance = 0;
		if(stops != null && stops.length > 1 ){
			for(int i = 0; i < stops.length - 1; i++){
				char start = Utils.toUpperCase(stops[i]);
				char end = Utils.toUpperCase(stops[i + 1]);
				int indextOfStart = -1;
				int indextOfEnd = -1;
				if(start == end || (indextOfStart = indextOf(start)) == -1 || (indextOfEnd = indextOf(end)) == -1){
					return -1;
				}
				Stop startStop = mStopsList.get(indextOfStart);
				Track nextTrack = startStop.getFirstTrack();
				while (nextTrack != null && nextTrack.getAdjStop() != indextOfEnd) {
					nextTrack = nextTrack.getNext();
				}
				if(nextTrack != null){
					distance += nextTrack.getDistance();
				}else {
					return INVALID_TRIP;
				}
			}
		}
		return distance;
	}
	
	public int distanceOf(String route){
		if(!Utils.isEmpty(route)){
			return distanceOf(route.toCharArray());
		}
		return INVALID_TRIP;
	}
	
	public ArrayList<String> tripsOfRoute(char start, char end, int maxStop){
		if(maxStop >= 1){
			ArrayList<String> trips = new ArrayList<>();
			Stack<Character> routeStack = new Stack<>();
			int step = 0;
			findNextStop(start, end, step, maxStop, routeStack, trips);
			return trips;
		}
		return null;
	}
	
	private void findNextStop(char start, char end, int step, int maxStop, Stack<Character> routeStack, ArrayList<String> trips){
		Stop startStop = mStopsList.get(indextOf(start));
		Track nextTrack = startStop.getFirstTrack();
		routeStack.push(start);
		step++;
		while (nextTrack != null) {
			Stop nextStop = mStopsList.get(nextTrack.getAdjStop());
			if(step <= maxStop){
				if(nextStop.getStopName() == end){
					Object[] array = routeStack.toArray();
					if(array != null){
						sBuilder.setLength(0);
						for (int i = 0; i < array.length; i++) {
							sBuilder.append(array[i]);
						}
						sBuilder.append(end);
						trips.add(sBuilder.toString());
					}
				}
				findNextStop(nextStop.getStopName(), end, step, maxStop, routeStack, trips);
			}
			nextTrack = nextTrack.getNext();
		}
		routeStack.pop();
		step--;
		if(routeStack.isEmpty() || step <= 0){
			return;
		}
	}

	public ArrayList<String> tripsOfRoute(char start, char end){
		ArrayList<String> trips = new ArrayList<>();
		Stack<Character> stack = new Stack<>();
		findRoute(start, end, trips, stack);
		return  trips;
	}

	private void findRoute(char start, char end, ArrayList<String> trips, Stack<Character> routeStack){
		Stop startStop = mStopsList.get(indextOf(start));
		Track nextTrack = startStop.getFirstTrack();
		routeStack.push(start);
		while (nextTrack != null){
			Stop nextStop = mStopsList.get(nextTrack.getAdjStop());
			char nextStopName = nextStop.getStopName();
			if(nextStopName == end){
				Object[] tripArray = routeStack.toArray();
				sBuilder.setLength(0);
				for (int i = 0; i < tripArray.length; i++) {
					sBuilder.append(tripArray[i]);
				}
				sBuilder.append(end);
				trips.add(sBuilder.toString());
			}else if(!routeStack.contains(nextStopName)){
				findRoute(nextStopName, end, trips, routeStack);
			}
			nextTrack = nextTrack.getNext();
		}
		routeStack.pop();
	}


	public String shortestRouteOf(char start, char end){
		ArrayList<String> trips = tripsOfRoute(start, end);
		if(!Utils.isEmpty(trips)){
			String minTrip = trips.get(0);
			int minTripDistance = distanceOf(minTrip);
			if(trips.size() > 1){
				for (int i = 1; i < trips.size(); i++) {
					String trip = trips.get(i);
					int distance = distanceOf(trip);
					minTripDistance = Math.min(distance, minTripDistance);
					minTrip = minTripDistance == distance ? trip : minTrip;
				}
			}
			return minTrip;
		}
		return null;
	}

	public int shortestDiatanceOf(char start, char end){
		int n = mStopsList.size();
		boolean visited[] = new boolean[n];
		int[] parent = new int[n];
		Node[] distanceNode = new Node[n];
		PriorityQueue<Node> queue = new PriorityQueue<>();
		int starIndex = indextOf(start);
		for (int i = 0; i < mStopsList.size(); i++){
			distanceNode[i] = new Node();
			distanceNode[i].distance = INF_DISTANCE;
			distanceNode[i].mStop = i;
			parent[i] = -1;
			visited[i] = false;
		}

		distanceNode[starIndex].distance = 0;
		queue.add(distanceNode[starIndex]);
		while (!queue.isEmpty()){
			Node cNode = queue.poll();
			int stop = cNode.mStop;
			if(visited[stop]){
				continue;
			}
			visited[stop] = true;
			Track track = mStopsList.get(stop).getFirstTrack();
			while (track != null){
				int adj = track.getAdjStop();
				if(!visited[adj] && distanceNode[adj].distance > distanceNode[stop].distance + track.getDistance()){
					distanceNode[adj].distance = distanceNode[stop].distance + track.getDistance();
					parent[adj] = stop;
					queue.add(distanceNode[adj]);
				}
				track = track.getNext();
			}
		}

		return distanceNode[indextOf(end)].distance;
	}

	public String shortestTripOf(char start, char end){
		int n = mStopsList.size();
		boolean visited[] = new boolean[n];
		int[] parent = new int[n];
		Node[] distanceNode = new Node[n];
		PriorityQueue<Node> queue = new PriorityQueue<>();
		int starIndex = indextOf(start);
		for (int i = 0; i < mStopsList.size(); i++){
			distanceNode[i] = new Node();
			distanceNode[i].distance = INF_DISTANCE;
			distanceNode[i].mStop = i;
			parent[i] = -1;
			visited[i] = false;
		}

		distanceNode[starIndex].distance = 0;
		queue.add(distanceNode[starIndex]);
		while (!queue.isEmpty()){
			Node cNode = queue.poll();
			int stop = cNode.mStop;
			if(visited[stop]){
				continue;
			}
			visited[stop] = true;
			Track track = mStopsList.get(stop).getFirstTrack();
			while (track != null){
				int adj = track.getAdjStop();
				if(!visited[adj] && distanceNode[adj].distance > distanceNode[stop].distance + track.getDistance()){
					distanceNode[adj].distance = distanceNode[stop].distance + track.getDistance();
					parent[adj] = stop;
					queue.add(distanceNode[adj]);
				}
				track = track.getNext();
			}
		}
		char p = end;
		if(distanceNode[indextOf(end)].distance < INF_DISTANCE) {
			sBuilder.setLength(0);
			do {
				sBuilder.append(p);
				int index = indextOf(p);
				int preStop = parent[index];
				p = mStopsList.get(preStop).getStopName();
			} while (p != start);
			sBuilder.append(start);
			return sBuilder.reverse().toString();
		}
		return null;
	}

	static class Node implements Comparable<Node>{
		int mStop;
		int distance;

		@Override
		public int compareTo(Node another) {
			if(another == null){
				return 1;
			}
			if(distance > another.distance){
				return 1;
			}else if(distance < another.distance){
				return -1;
			}else {
				return 0;
			}
		}
	}

	public ArrayList<Stop> getStops(){
		return mStopsList;
	}

	public Stop findStopWithName(char name){
		for (Stop stop : mStopsList){
			if(stop.getStopName() == name){
				return stop;
			}
		}
		return null;
	}


}
