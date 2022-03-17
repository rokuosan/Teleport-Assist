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
    // エラーメッセージ
    val NOT_FOUND_TARGET_PLAYER = "${ChatColor.ITALIC}[!!] 対象のプレイヤーが見つかりません。"
    val UNKNOWN_ARGUMENTS = "${ChatColor.ITALIC}[!!] 不明な引数です。"
    val FATAL_ERROR = "${ChatColor.RED}${ChatColor.BOLD} [!!] 致命的なエラーが発生しました。"
    val DOESNT_HAVE_PERMISSION = "${ChatColor.ITALIC}[!!] 権限がありません。"
    val NOT_FOUND_REQUEST = "${ChatColor.ITALIC}[!!] リクエストが見つかりません。"
    val REQUEST_HAS_ALREADY_SENT = "${ChatColor.ITALIC}[!!] 既にリクエストを送っています。"
    val CANNOT_REQUEST_SELF = "${ChatColor.ITALIC}[!!] 自身にリクエストを送信することはできません。"


    // 警告メッセージ
    val TOO_MANY_ARGUMENTS = "${ChatColor.ITALIC}[!] 引数が多すぎます。"
    val REQUIRE_TARGET_PLAYER = "${ChatColor.ITALIC}[!] 対象のプレイヤーを指定してください。"


    // 成功メッセージ
    val PERMIT_TELEPORT = "${ChatColor.ITALIC}[=] テレポートリクエストを受理しました。"
    val SUCCESSFULLY_TELEPORT = "${ChatColor.ITALIC}[=] テレポートに成功しました。"


    // そのほか
    fun requestNotification(name: String) = "${ChatColor.ITALIC}[?] ${name}からテレポートリクエストを受け取りました。"
    fun sentNotification(name: String) = "${ChatColor.ITALIC}[?] ${name}にテレポートリクエストを送信しました。"
}