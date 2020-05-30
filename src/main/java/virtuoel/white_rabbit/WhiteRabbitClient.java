package virtuoel.white_rabbit;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import virtuoel.white_rabbit.init.ItemRegistrar;

public class WhiteRabbitClient implements ClientModInitializer
{
	@Override
	public void onInitializeClient()
	{
		ColorProviderRegistry.ITEM.register((stack, tintIndex) ->
		{
			return tintIndex > 0 ? -1 : 0x00F5F5DC;
		},
		ItemRegistrar.PISHSALVER);
	}
}
