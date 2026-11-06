package kr.fasol.obix.core.util;

import kr.fasol.obix.core.config.BusinessTierHeaderInfoProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua_parser.Client;
import ua_parser.Parser;

/**
 * UserAgent를 처리하는 Util
 */
@Component
public class UserAgentUtil {

    private static BusinessTierHeaderInfoProvider businessTierHeaderInfoProvider;

    @Autowired
    public UserAgentUtil(BusinessTierHeaderInfoProvider provider) {
        businessTierHeaderInfoProvider = provider;
    }

    public static Client getClient() {
        Parser parser = new Parser();
        return parser.parse(businessTierHeaderInfoProvider.getUserAgent());
    }

}
