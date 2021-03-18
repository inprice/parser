import org.apache.commons.lang3.StringUtils;

import io.inprice.common.models.LinkSpec;

public class Test {

	public static void main(String[] args) {
		String[] vals = { "1.Selami", "Selami 23.", "1.Selami 23.", "1.Sela 3332. mi 23.", "  2341.Sela 3332. mi 23." };
		for (String val : vals) {
			System.out.println(val.trim().replaceFirst("^[0-9]{1,}\\.", ""));
		}
	}
	
	public static void main1(String[] args) {
		String value = "● Anti-Scratch, Anti-Slip: Flexible, shock-resistant elastomers provide the perfect balance and protection from scratches and impacts. The anti-slip details on two flanks avoids the case slipping out of your hand, protect from damage. ● Added Carabiner: Durable carabiner is included with case. Just enjoy the outdoors, Take your airpods with you during all activities: running, hiking, climbing, camping etc.. Slim form-fitting design allows for easy installation and continuous use without the added bulk and hassle of carrying an additional case.";
		
		String key = "";
		
		String[] features = value.split("●");
		for (int j = 0; j < features.length; j++) {
			String val = features[j];
			if (StringUtils.isBlank(val)) continue;
			if (val.indexOf(":") > 0) {
				String[] pair = val.split(":");
				key = pair[0];
				val = pair[1];
				System.out.println(key + "-" + val);
			} else {
				System.out.println(val);
			}
		}
		
	}
	
}
