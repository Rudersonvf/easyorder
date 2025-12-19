package br.com.ruderson.easyorder.dto.home;

import br.com.ruderson.easyorder.dto.store.StoreResumeDTO;
import br.com.ruderson.easyorder.dto.user.UserResumeDTO;

public record HomeResponse(
    StoreResumeDTO store,
    UserResumeDTO user
) {
}
