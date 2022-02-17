package com.castaware.nocturne.datatypes;

import java.util.List;
import java.util.Map.Entry;

import eic.tcc.dao.Dao;

import java.util.TreeMap;

@SuppressWarnings({"serial","unused"})
public class GameMap extends TreeMap<String,Game>
{	
	private Dao dao;
	private Wallet wallet;
	
	public GameMap(Dao dao, Wallet wallet)
	{
		this.dao=dao;
		this.wallet=wallet;
		
		List<Game> games = dao.retrieveBySingleLike(Game.class, "wallet", wallet);
		
		for (Game game : games) 
		{
			this.put(game.getName(),game);
		}
	}
	
	public void clear()
	{
		for (Game game : values()) 
		{
			game.clear();
		}
	}
	
	public void persist()
	{
//		for (Game game : values()) 
//		{
//			dao.persist(game);
//		}
	}
	
	public Game getGame(String gameName)
	{
		Game game = this.get(gameName);
		
		if (game==null)
		{
			try 
			{
				game = new Game(gameName);
			}
			catch (Exception e) 
			{
				throw new IllegalStateException(e);
			}
			
//			dao.persist(game);
			put(gameName,game);			
		}
		
		return game;			
	}
	
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		
		builder.append("###### GAMES ######");
		for(Entry<String,Game> entry : entrySet())
		{
			builder.append("\n# "+entry.getValue());
		}
		builder.append("\n#");
		
		return builder.toString();
	}
}
