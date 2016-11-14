package com.minehut.warzone.rotation;

import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import com.minehut.cloud.bukkit.util.json.JsonUtil;
import com.minehut.warzone.Warzone;
import com.minehut.warzone.rotation.exception.RotationLoadException;
import com.minehut.warzone.util.Contributor;
import org.apache.commons.io.Charsets;
import org.bukkit.Bukkit;
import org.jdom2.Document;
import org.jdom2.Element;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.logging.Level;

public class Rotation {

    private File rotationFile;
    private List<LoadedMap> rotation;
    private List<LoadedMap> loaded;
    private int position;
    private File repo;

    public Rotation() throws RotationLoadException {
        this.rotationFile = new File(Warzone.getInstance().getConfig().getString("rotation"));
        try {
            refreshRepo();
        } catch (IOException e) {
            throw new RotationLoadException("Could not access the repository. Make sure java has sufficient read and write privileges.");
        }
        refreshRotation();
    }

    /**
     * Refreshes the maps in the repository and the data associated with them
     *
     * @throws RotationLoadException
     */
    public void refreshRepo() throws RotationLoadException, IOException {
        loaded = new ArrayList<>();
        this.repo = new File(Warzone.getInstance().getConfig().getString("repo"));
        if (!repo.exists()) repo.mkdir();
        List<String> requirements = Arrays.asList("map.json", "region", "level.dat");
        if (repo.listFiles() != null) {
            for (File map : repo.listFiles()) {
                if (map.isFile()) continue;
                if (Arrays.asList(map.list()).containsAll(requirements)) {
                    try {
                        JsonObject json = JsonUtil.convertFileToJSON(map.getPath() + "/map.json");

                        String name = json.get("name").getAsString();
                        String builder = json.get("author").getAsString();

                        loaded.add(new LoadedMap(name, builder, 100, map));
                    } catch (Exception e) {
                        Bukkit.getLogger().log(Level.WARNING, "Failed to load map at " + map.getAbsolutePath());
                        if (Warzone.getInstance().getConfig().getBoolean("displayMapLoadErrors")) {
                            Bukkit.getLogger().log(Level.INFO, "Showing error, this can be disabled in the config: ");
                            e.printStackTrace();
                        }
                    }
                }

                else {
                    if (map.listFiles() != null) {
                        for (File innerMap : map.listFiles()) {
                            if (innerMap.isFile()) continue;
                            if (Arrays.asList(innerMap.list()).containsAll(requirements)) {
                                try {
                                    JsonObject json = JsonUtil.convertFileToJSON(innerMap.getPath() + "/map.json");

                                    String name = json.get("name").getAsString();
                                    String builder = json.get("author").getAsString();

                                    loaded.add(new LoadedMap(name, builder, 100, innerMap));
                                } catch (Exception e) {
                                    Bukkit.getLogger().log(Level.WARNING, "Failed to load map at " + innerMap.getAbsolutePath());
                                    if (Warzone.getInstance().getConfig().getBoolean("displayMapLoadErrors")) {
                                        Bukkit.getLogger().log(Level.INFO, "Showing error, this can be disabled in the config: ");
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
//        updatePlayers();
        if (loaded.size() < 1)
            throw new RotationLoadException("No maps were loaded. Are there any maps in the repository?");
    }

    /**
     * Refreshes the plugin's default rotation
     */
    public void refreshRotation() throws RotationLoadException {
        rotation = new ArrayList<>();
        try {
            if (!rotationFile.exists()) {
                List<String> maps = Lists.newArrayList();
                for (LoadedMap map : loaded) maps.add(map.getName());
                FileWriter writer = new FileWriter(rotationFile);
                for (String map : maps) writer.write(map + System.lineSeparator());
                writer.close();
            }
            List<String> lines = Files.readAllLines(rotationFile.toPath(), Charsets.UTF_8);
            if (lines.size() < 1) {
                for (int x = 0; x < 8; x++) {
                    try {
                        rotation.add(loaded.get(x));
                    } catch (IndexOutOfBoundsException ex) {
                    }
                }
                Bukkit.getLogger().log(Level.WARNING, "Failed to load rotation file, using a temporary rotation instead.");
            } else {
                for (String line : lines) {
                    for (LoadedMap map : loaded) {
                        if (map.getName().replaceAll(" ", "").equalsIgnoreCase(line.replaceAll(" ", ""))) {
                            rotation.add(map);
                            break;
                        }
                    }
                }
            }
        } catch (IOException e) {
            for (int x = 0; x < 8; x++) {
                try {
                    rotation.add(loaded.get(x));
                } catch (IndexOutOfBoundsException ex) {
                }
            }
            Bukkit.getLogger().log(Level.WARNING, "Failed to load rotation file, using a temporary rotation instead.");
        }
        position = 0;
    }

    /**
     * Move the position in the rotation by one. If the end of the rotation is reached, it will be automatically reset.
     */
    public int move() {
        position++;
        if (position > rotation.size() - 1) {
            position = 0;
        }
        return position;
    }

    /**
     * @return Returns the next map in the rotation
     */
    public LoadedMap getNext() {
        return rotation.get(position);
    }

    /**
     * @return Returns the rotation
     */
    public List<LoadedMap> getRotation() {
        return rotation;
    }

    /**
     * @return Returns all loaded maps
     */
    public List<LoadedMap> getLoaded() {
        return loaded;
    }

    /**
     * @return Gets the rotation index of the next map
     */
    public int getNextIndex() {
        return position;
    }

    /**
     * @param string The name of map to be searched for
     * @return The map
     */
    public LoadedMap getMap(String string) {
        for (LoadedMap map : loaded) {
            if (map.getName().toLowerCase().startsWith(string.toLowerCase())) return map;
        }
        return null;
    }

    public LoadedMap getCurrent() {
        try {
            return rotation.get(position - 1);
        } catch (IndexOutOfBoundsException e) {
            return rotation.get(0);
        }
    }

    private Contributor parseContributor(Element element) {
        if (element.getAttributeValue("uuid") != null) {
            return new Contributor(UUID.fromString(element.getAttributeValue("uuid")), element.getAttributeValue("contribution"));
        } else return new Contributor(element.getText(), element.getAttributeValue("contribution"));
    }

//    @SuppressWarnings("unchecked")
//    private void updatePlayers() {
//        Bukkit.getScheduler().runTaskAsynchronously(Cardinal.getInstance(), new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Bukkit.getLogger().log(Level.INFO, "Setting known map author names.");
//                    HashMap<UUID, String> names;
//                    try {
//                        names = (HashMap<UUID, String>) new ObjectInputStream(new FileInputStream(new File(Cardinal.getInstance().getDataFolder().getPath() + "/.names.ser"))).readObject();
//                    } catch (FileNotFoundException e) {
//                        names = Maps.newHashMap();
//                    }
//                    for (LoadedMap map : loaded) {
//                        List<Contributor> contributors = new ArrayList<>();
//                        contributors.addAll(map.getAuthors());
//                        contributors.addAll(map.getContributors());
//                        for (Contributor contributor : contributors) {
//                            if (contributor.getName() == null && contributor.getUniqueId() != null) {
//                                if (!names.containsKey(contributor.getUniqueId())) names.put(contributor.getUniqueId(), "Unknown");
//                                contributor.setName(names.get(contributor.getUniqueId()));
//                            }
//                        }
//                    }
//                    Bukkit.getLogger().log(Level.INFO, "Updating map author names.");
//                    List<UUID> requested = Lists.newArrayList();
//                    for (LoadedMap map : loaded) {
//                        List<Contributor> contributors = new ArrayList<>();
//                        contributors.addAll(map.getAuthors());
//                        contributors.addAll(map.getContributors());
//                        for (Contributor contributor : contributors) {
//                            if (contributor.getUniqueId() == null) continue;
//                            if (requested.contains(contributor.getUniqueId())) {
//                                contributor.setName(names.get(contributor.getUniqueId()));
//                            } else {
//                                OfflinePlayer player = Bukkit.getOfflinePlayer(contributor.getUniqueId());
//                                if (player != null && player.getLastPlayed() + 180000 > System.currentTimeMillis()) {
//                                    names.put(contributor.getUniqueId(), player.getName());
//                                } else {
//                                    String name = MojangUtil.getName(contributor.getUniqueId());
//                                    if (name != null) {
//                                        names.put(contributor.getUniqueId(), name);
//                                        requested.add(contributor.getUniqueId());
//                                    }
//                                }
//                                contributor.setName(names.get(contributor.getUniqueId()));
//                            }
//                        }
//                        if (map.equals(GameHandler.getGameHandler().getMatch().getLoadedMap())) {
//                            GameHandler.getGameHandler().getMatch().getModules().getModule(HeaderModule.class).updateHeader();
//                            GameHandler.getGameHandler().getMatch().getModules().getModule(HeaderModule.class).updateAll();
//                        }
//                    }
//                    ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(new File(Cardinal.getInstance().getDataFolder().getPath() + "/.names.ser")));
//                    out.writeObject(names);
//                    out.close();
//                    Bukkit.getLogger().log(Level.INFO, "Finnished updating map author names.");
//                } catch (IOException | ClassNotFoundException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }

}
