package com.kika.typinggame;

import java.util.ArrayList;
import java.util.List;


public class CompositeLoader implements GamePanelSettingsLoader
{
	private List<GamePanelSettingsLoader> loaders;
	
	public CompositeLoader(List<GamePanelSettingsLoader> loaders)
	{
		this.loaders = loaders;
	}
	
	public List<GamePanelSettings> load()
	{
		List<GamePanelSettings> settingsList = new ArrayList<GamePanelSettings>();

		for (GamePanelSettingsLoader loader : loaders)
		{		
			List<GamePanelSettings> loaderList = loader.load();
			settingsList.addAll(loaderList);
		}
		
		return settingsList;
	}

}
