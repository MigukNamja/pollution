package miguknamja.pollution.network;


import io.netty.buffer.ByteBuf;
import miguknamja.pollution.data.ClientData;
import miguknamja.pollution.data.PollutionDataValue;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSendPollution implements IMessage {
    private PollutionDataValue pdv;

    public PacketSendPollution() {}
    
    public PacketSendPollution( PollutionDataValue pdv ) {
    	this.pdv = pdv;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        pdv = new PollutionDataValue(buf.readDouble());
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeDouble(pdv.pollutionLevel);
    }

    public static class Handler implements IMessageHandler<PacketSendPollution, IMessage> {
        @Override
        public IMessage onMessage(PacketSendPollution message, MessageContext ctx) {
            // Always use a construct like this to actually handle your message. This ensures that
            // your 'handle' code is run on the main Minecraft thread. 'onMessage' itself
            // is called on the networking thread so it is not safe to do a lot of things
            // here.
            //FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx)); // for receiving on server
        	IThreadListener mainThread = Minecraft.getMinecraft();
        	mainThread.addScheduledTask(() -> handle(message, ctx));
        	return null;
        }

        private void handle(PacketSendPollution message, MessageContext ctx) {
            // This code is run on the client side. So you can do client-side calculations here
        	ClientData.pdv = message.pdv;
        }
    }
}
