package com.deviseworks.tpa.common

import org.bukkit.ChatColor

/**
 * 出力文字列を管理する。
 * 命名規則は原則として以下の通り。
 *
 * - エラー時プレフィックス: [!!]
 * - 警告時プレフィックス: [!]
 * - 成功時プレフィックス: [=]
 * - そのほか: [?]
 */
object Messages {
    // プレフィックス
    private val ERROR = "${ChatColor.RED}${ChatColor.ITALIC}[!!]"
    private val WARN = "${ChatColor.ITALIC}[!]"
    private val HINT = "${ChatColor.ITALIC}[?]"
    private val SUCCESS = "${ChatColor.GREEN}{ChatColor.ITALIC}[=]"


    // エラーメッセージ
    val NOT_FOUND_TARGET_PLAYER = "$ERROR 対象のプレイヤーが見つかりません。"
    val UNKNOWN_ARGUMENTS = "$ERROR 不明な引数です。"
    val FATAL_ERROR = "$ERROR 致命的なエラーが発生しました。"
    val DOESNT_HAVE_PERMISSION = "$ERROR 権限がありません。"
    val NOT_FOUND_REQUEST = "$ERROR リクエストが見つかりません。"
    val REQUEST_HAS_ALREADY_SENT = "$ERROR 既にリクエストを送っています。"
    val CANNOT_REQUEST_SELF = "$ERROR 自身にリクエストを送信することはできません。"


    // 警告メッセージ
    val TOO_MANY_ARGUMENTS = "$WARN 引数が多すぎます。"
    val REQUIRE_TARGET_PLAYER = "$WARN 対象のプレイヤーを指定してください。"
    val REQUEST_TIMEOUT = "$WARN リクエストの期限が切れました"


    // 成功メッセージ
    val PERMIT_TELEPORT = "$SUCCESS テレポートリクエストを受理しました。"
    val SUCCESSFULLY_TELEPORT = "$SUCCESS テレポートに成功しました。"


    // そのほか
    fun requestNotification(name: String) = "$HINT ${name}からテレポートリクエストを受け取りました。"
    fun sentNotification(name: String) = "$HINT ${name}にテレポートリクエストを送信しました。"
    val HINT_HOW_TO_ACCEPT = "$HINT 承諾するには${ChatColor.YELLOW}${ChatColor.UNDERLINE}/tpa <名前> ${ChatColor.RESET}${ChatColor.ITALIC}を実行してください。"
    val HINT_RESEND_REQUEST = "$HINT 期限切れの場合はもう一度リクエストを受け取る必要があります"
}