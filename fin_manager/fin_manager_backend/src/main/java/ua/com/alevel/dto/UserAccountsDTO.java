package ua.com.alevel.dto;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import ua.com.alevel.persistence.entity.Account;
import ua.com.alevel.persistence.entity.User;

import java.util.Collection;
import java.util.Set;

@Getter
@Setter
public class UserAccountsDTO extends UserDTO {

    private Collection<AccountDTO> accounts;

    public UserAccountsDTO() {
    }

    public UserAccountsDTO(User user) {
        super(user);
        if (CollectionUtils.isNotEmpty(user.getAccounts())) {
            Set<Account> accountSet = user.getAccounts();
            this.accounts = accountSet
                    .stream()
                    .map(AccountDTO::new)
                    .toList();
        }
    }
}
