package miguknamja.pollution.commands;

import java.util.ArrayList;
import java.util.List;

import miguknamja.pollution.Config;
import miguknamja.pollution.data.PollutionDataValue;
import miguknamja.pollution.data.PollutionWorldData;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.UserListOps;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class PollutionCommand implements ICommand {

    private final ArrayList<String> aliases;
    protected String usageString;
    
    public PollutionCommand() {
		aliases = new ArrayList<String>();
		aliases.add("pollution");
		aliases.add("poll");
		usageString = "pollution clear [all]\n" +
	                  "          get\n" +
		              "          add <percent>\n" +
		              "          sub <percent>\n" +
		              "          set <amount>";
	}
	
    protected void send( ICommandSender sender, String string ) {
    	sender.addChatMessage(new TextComponentString(TextFormatting.WHITE + string));
    }

    protected void usage( ICommandSender sender ) {
    	send( sender, usageString);    	
    }
    
    
	@Override
	public int compareTo(ICommand arg0) {
		return 0;
	}

	@Override
	public String getCommandName() {
		return "pollution";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return usageString;
	}

	@Override
	public List<String> getCommandAliases() {
		return this.aliases;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		World world = sender.getEntityWorld(); 
		if (world.isRemote) { return; } 
		//Logging.log("Pollution Commands.execute() : " + args);

		Chunk chunk = world.getChunkFromBlockCoords(sender.getPosition());

		switch( args.length ) {
		case 0:
			send( sender, "I hate to argue, but you need arguments to 'pollution'." );
			usage( sender );
			break;
		case 1:
			switch( args[0] ) {
			case "clear":
				PollutionWorldData.clear( world, chunk );
				send( sender, "Cleared all pollution in the current chunk."); 
				break;
			case "get":
				PollutionDataValue pdv = PollutionWorldData.getPollution(world, chunk);
				double percent = PollutionWorldData.getPollutionPercent(world, chunk);
				send( sender, "Pollution in current chunk : " + pdv.pollutionLevel + ", " + percent + "% of maximum."); 
				break;
			default:
				usage( sender );
				break;
			}
			break;
		case 2:			
			switch( args[0] ) {
			case "clear":
				switch( args[1] ) {
				case "all":
					PollutionWorldData.clear( world );
					send( sender, "Cleared all pollution in the current dimension.");
					break;
				default:
					send( sender, "Only 'clear all' is valid syntax.");
					usage( sender );
					break;
				}
				break;
			case "add":
			case "sub":
				double input = Double.parseDouble( args[1] );
				double newValue = 0.0;
				if( input < 0.0 || input > 100.0 ) {
					send( sender, "Percent must be between 0.0 and 100.0."); 
					usage( sender );
					return;
				}
				switch( args[0] ) {
				case "add":				
					newValue = PollutionWorldData.changePercent( input, world, chunk );
					send( sender, "Increased pollution in the current chunk by " + input + "% to " + newValue + ".");
					break;
				case "sub":				
					newValue = PollutionWorldData.changePercent( 0.0 - input, world, chunk );
					send( sender, "Decreased pollution in the current chunk by " + input + "% to " + newValue + ".");
					break;
				default:
					break;
				}
				break;
			case "set":
				double pollution = Double.parseDouble( args[1] );
				if( pollution < Config.minPollutionLevel || pollution > Config.maxPollutionLevel ) {
					send( sender, "Percent must be between " + Config.minPollutionLevel + " and " + Config.maxPollutionLevel + "."); 
					usage( sender );
					return;
				}
				PollutionWorldData.setPollution(new PollutionDataValue(pollution), world, chunk);
				send( sender, "Set pollution in current chunk to " + pollution ); 
				break;
			default:
				usage( sender );
				break;
			}
			break;
		default:
			usage( sender );
			break;
		}
		return;
	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		if (sender.getEntityWorld().isRemote) { return false; } // only run this command on the server 

		if( server.isDedicatedServer() ) {
			UserListOps ops = server.getPlayerList().getOppedPlayers();
			if( ops.getGameProfileFromName(sender.getName()) != null ) { return true; }
			else { return false; }
		} else {
			return true; // always return true on single player
		}
	}

	@Override
	public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
		return null;
	}

	@Override
	public boolean isUsernameIndex(String[] args, int index) {
		return false;
	}

}
