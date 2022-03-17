package com.deviseworks.tpa.entity

import java.util.*

data class PlayerStatus(
    // プレイヤーのUUID
    var uuid: UUID,

    // プレイヤーが待機状態か
    var isWaiting: Boolean,

    // リクエストされたプレイヤーID
    var targetUUID: UUID,

    // リクエストされたプレイヤーが許可したか
    var accepted: Boolean,

    // 期限切れか
    var expired:Boolean = false
)
