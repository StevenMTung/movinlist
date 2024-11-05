package br.com.steventung.movinlist.domain.modelDocument

import br.com.steventung.movinlist.domain.model.User

data class UserDocument(
    val name: String = "",
    val picture: String = ""
) {
    fun toUser(userId: String) = User(
        userId = userId,
        name = name,
        picture = picture
    )
}
