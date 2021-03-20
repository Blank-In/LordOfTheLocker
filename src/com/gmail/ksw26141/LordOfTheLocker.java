package com.gmail.ksw26141;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

@SuppressWarnings({"unchecked","deprecation"})
public class LordOfTheLocker extends JavaPlugin implements Listener{
	String PluginTitle=ChatColor.RED+"["+ChatColor.WHITE+"LordOfTheLocker"+ChatColor.RED+"] "+ChatColor.GRAY;
	String GreenTitle=ChatColor.WHITE+"["+ChatColor.GREEN+" ! "+ChatColor.WHITE+"] "+ChatColor.GRAY;
	String RedTitle=ChatColor.WHITE+"["+ChatColor.DARK_RED+" ! "+ChatColor.WHITE+"] "+ChatColor.GRAY;
	String LockerTitle=ChatColor.DARK_GRAY+""+ChatColor.BOLD;
	String autoSaveConfig="setting.autosave", autoTime="setting.autotime", autoMessageConfig="setting.automessage";
	
	ItemStack AIR=new ItemStack(Material.AIR);
	BukkitScheduler scheduler = getServer().getScheduler();
	
	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
		getLogger().info(PluginTitle+"작동 시작");
		
		if(!getConfig().isSet(autoSaveConfig)) {//자동저장 토글
			getConfig().set(autoSaveConfig, true);
		}
		if(!getConfig().isSet(autoMessageConfig)) {//자동저장 토글
			getConfig().set(autoMessageConfig, true);
		}
		if(!getConfig().isSet(autoTime)) {//자동저장 시간
			getConfig().set(autoTime, 10);
		}
		autoSave();//자동저장 
	}
	@Override
	public void onDisable() {
		saveConfig();
		getLogger().info(PluginTitle+"작동 중지");
	}
	
	public void sendMessage(CommandSender sender,String message) {
		sender.sendMessage(message);
		getLogger().info(message);
	}
	public void sendAllMessage(String message) {
		for(Player user:getServer().getOnlinePlayers()) {
			user.sendRawMessage(message);
		}
		getLogger().info(message);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] player){
		Player user=getServer().getPlayer(sender.getName());
		
		if(cmd.getName().equalsIgnoreCase("보관함")||cmd.getName().equalsIgnoreCase("locker")) { //보관함 명령어
			ArrayList<ItemStack> list=(ArrayList<ItemStack>)getConfig().get("locker."+user.getUniqueId());
			Inventory locker=Bukkit.createInventory(null, 9,LockerTitle+user.getName()+"님의 보관함");
			
			if(getConfig().isSet("locker."+user.getUniqueId())) { //콘픽에 보관함이 저장되어 있다면
				for(int a=0;a<list.size();++a) {
					if(locker.firstEmpty()==-1) {
						break;
					}
					if(list.get(a)!=null) {
						locker.setItem(locker.firstEmpty(), list.get(a));
					}
				}
			}
			user.openInventory(locker);
			return true;
		}
		
		else if(cmd.getName().equalsIgnoreCase("givetolocker")) { //보관함 아이템 증정 명령어
			ItemStack item=user.getItemInHand();
			if(player.length==0) {
				for(Player onlineUser:getServer().getOnlinePlayers()) {
					lockerAddItem(onlineUser, item);
				}
				sendMessage(sender,PluginTitle+"접속해 있는 모든 유저들의 인벤토리/보관함에 손에있는 아이템을 넣었습니다.");
				return true;
			}
			else if(player.length==1) {
				try {
					lockerAddItem(getServer().getPlayer(player[0]), item);
				}
				catch(Exception e) {
					sendMessage(sender, PluginTitle+"해당 유저가 서버에 접속해 있지 않습니다.");
					return true;
				}
				sendMessage(sender,PluginTitle+player[0]+"의 인벤토리/보관함에 손에있는 아이템을 넣었습니다.");
				return true;
			}
		}
		
		else if(cmd.getName().equalsIgnoreCase("lockersave")) { //보관함 저장 명령어
			sendMessage(sender, PluginTitle+"유저 보관함 정보 저장중. 저장이 지나치게 오래걸린다면 유저의 보관함 데이터가 과도하게 쌓였을 가능성이 있습니다. 유저들에게 아이템 수령을 요청하거나 서버의 플러그인 폴더에서 해당 플러그인의 폴더 안의 config.yml을 삭제하여 보관함 정보를 초기화 하는것을 추천합니다.");
			saveConfig();
			sendMessage(sender, PluginTitle+"보관함의 내용이 저장되었습니다!");
			return true;
		}
		
		else if(cmd.getName().equalsIgnoreCase("lockerautosave")) {
			if(player.length==0) {
				String message="현재 자동저장이 ";
				message+=ChatColor.YELLOW+""+getConfig().getInt(autoTime)+ChatColor.GRAY+"분 간격으로 ";
				if(getConfig().getBoolean(autoSaveConfig)) {
					message+=ChatColor.YELLOW+"켜져 있습니다.";
				}
				else {
					message+=ChatColor.YELLOW+"꺼져 있습니다.";
				}
				sender.sendMessage(PluginTitle+message);
				return true;
			}
			else if(player.length==1) {
				String toggle=player[0];
				if(toggle.equals("on")||toggle.equals("true")) {
					getConfig().set(autoSaveConfig, true);
					sender.sendMessage(PluginTitle+"자동저장을 켭니다.");
					return true;
				}
				else if(toggle.equals("off")||toggle.equals("false")) {
					getConfig().set(autoSaveConfig, false);
					sender.sendMessage(PluginTitle+"자동저장을 끕니다.");
					return true;
				}
			}
		}
		
		else if(cmd.getName().equalsIgnoreCase("lockerautomessage")) {
			if(player.length==0) {
				String message="현재 자동 수령알림이 ";
				message+=ChatColor.YELLOW+""+getConfig().getInt(autoTime)+ChatColor.GRAY+"분 간격으로 ";
				if(getConfig().getBoolean(autoMessageConfig)) {
					message+=ChatColor.YELLOW+"켜져 있습니다.";
				}
				else {
					message+=ChatColor.YELLOW+"꺼져 있습니다.";
				}
				sender.sendMessage(PluginTitle+message);
				return true;
			}
			else if(player.length==1) {
				String toggle=player[0];
				if(toggle.equals("on")||toggle.equals("true")) {
					getConfig().set(autoMessageConfig, true);
					sender.sendMessage(PluginTitle+"자동 수령알림을 켭니다.");
					return true;
				}
				else if(toggle.equals("off")||toggle.equals("false")) {
					getConfig().set(autoMessageConfig, false);
					sender.sendMessage(PluginTitle+"자동 수령알림을 끕니다.");
					return true;
				}
			}
		}
		
		else if(cmd.getName().equalsIgnoreCase("lockerautotime")) {
			if(player.length==0) {
				sender.sendMessage(PluginTitle+"자동 시스템의 간격이 "+ChatColor.YELLOW+""+getConfig().getInt(autoTime)+ChatColor.GRAY+"분으로 설정되어있습니다.");
				return true;
			}
			else if(player.length==1) {
				try {
					int num=Integer.parseInt(player[0]);
					if(num<1) {
						sender.sendMessage(PluginTitle+"1보다 작은 숫자로 설정할 수 없습니다.");
					}
					else if(num>10000) {
						sender.sendMessage(PluginTitle+"지나치게 큰 숫자는 설정할 수 없습니다.");
					}
					else {
						getConfig().set(autoTime, num);
						sender.sendMessage(PluginTitle+"자동 시스템의 간격이 "+num+"분으로 변경되었습니다.");
					}
				}
				catch (Exception e) {
					return false;
				}
				return true;
			}
		}
		
		return false;
	}
	
	public void lockerAddItem(Player user,ItemStack item) {
		if(user.getInventory().firstEmpty()!=-1) { //인벤토리에 빈 공간이 있을 경우 인벤토리에 넣는다
			user.getInventory().setItem(user.getInventory().firstEmpty(), item);
			user.sendRawMessage(GreenTitle+"당신의 인벤토리에 아이템이 들어왔습니다.");
			return;
		}
		//인벤토리에 빈 공간이 없을 경우
		ArrayList<ItemStack> list=new ArrayList<ItemStack>();
		if(!getConfig().isSet("locker."+user.getUniqueId())) { //콘픽에 보관함이 저장되어 있지 않을 경우
			list=new ArrayList<ItemStack>();
			getConfig().set("locker."+user.getUniqueId(), list);
		}
		list=(ArrayList<ItemStack>)getConfig().get("locker."+user.getUniqueId());
		Boolean flg=true;
		for(int a=0;a<list.size();++a) {
			if(list.get(a)==null) {
				list.set(a, item);
				flg=false;
				break;
			}
		}
		if(flg) {
			list.add(item);
		}
		getConfig().set("locker."+user.getUniqueId(),list);
		user.closeInventory();
		user.playSound(user.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 1.0f, 1.0f);
		user.sendRawMessage(GreenTitle+"당신의 보관함에 아이템이 들어왔습니다.");
	}
	
	@EventHandler
	public void onInventoryClickEvent(InventoryClickEvent event) {
		Inventory inventory=event.getInventory();
		Inventory locker=event.getClickedInventory();
		if(inventory==null||locker==null) {
			return;
		}
		Player user=getServer().getPlayer(event.getWhoClicked().getName());
		if(inventory.getTitle().equals(LockerTitle+user.getName()+"님의 보관함")) {
			event.setCancelled(true);
			if(locker.getTitle().equals(LockerTitle+user.getName()+"님의 보관함")) {
				if(user.getInventory().firstEmpty()!=-1&&locker.getItem(event.getSlot())!=null) {
					user.playSound(user.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 1.0f, 1.0f);
					ArrayList<ItemStack> list=(ArrayList<ItemStack>) getConfig().get("locker."+user.getUniqueId());
					ItemStack item=locker.getItem(event.getSlot());
					
					list.remove(item);
					locker.setItem(event.getSlot(), AIR);
					user.getInventory().setItem(user.getInventory().firstEmpty(), item);
					if(list.size()==0) {
						getConfig().set("locker."+user.getUniqueId(), null);
					}
					else {
						getConfig().set("locker."+user.getUniqueId(), list);
					}
				}
			}
		}
	}
	
	public void autoSave() {
		scheduler.scheduleSyncDelayedTask(this, new Runnable() {
			@Override
			public void run() {
				if(getConfig().getBoolean(autoSaveConfig)) {
					getLogger().info("유저 보관함 정보 저장중. 저장이 지나치게 오래걸린다면 유저의 보관함 데이터가 과도하게 쌓였을 가능성이 있습니다. 유저들에게 아이템 수령을 요청하거나 서버의 플러그인 폴더에서 해당 플러그인의 폴더 안의 config.yml을 삭제하여 보관함 정보를 초기화 하는것을 추천합니다.");
					saveConfig();
					getLogger().info("유저 보관함 정보 저장완료.");
				}
				if(getConfig().getBoolean(autoMessageConfig)) {
					for(Player user:getServer().getOnlinePlayers()) {
						if(getConfig().isSet("locker."+user.getUniqueId())) {
							user.sendRawMessage(RedTitle+"당신의 보관함에 아이템이 있습니다 /보관함 또는 /locker 명령어로 아이템을 수령하십시오.");
						}
					}
				}
				autoSave();
			}
			
		},20*60*getConfig().getInt(autoTime)+1);//20틱 == 1초
	}

}