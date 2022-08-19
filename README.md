# Teleport Assist (TPA)

## Overview

このプラグインはユーザ間のテレポート機能を提供します。

## Supported Versions

- 1.18.2
- 1.18.1
- 1.18

## Installation

Releaseから最新のビルドをDLしてください。

あとはサーバーのpluginsに入れるだけで動きます。

## Usage

以下のコマンドでテレポート先のユーザにテレポートリクエストを送信します。
```shell
tpr テレポート先ユーザ名
```

リクエストを受理する場合は以下のコマンドを実行してください
```shell
tpa リクエストしたユーザ
```

権限``tpa.bypass.accept``を持ったユーザの場合、
コマンドに``-f``オプションを追加することでリクエストの受理を待たずに直接テレポートすることが可能です。

```shell
tpa -f リクエストしたユーザ
```

## Permission

| Permission        | Default | Description               |
|-------------------|---------|---------------------------|
| tpa.request       | true    | リクエストに必要な権限               |
| tpa.accept        | true    | リクエストを受理するための権限           |
| tpa.bypass.accept | op      | リクエストの受理を待たずにテレポートするための権限 |


## Contribution

このプラグインは適当に書いたものなので、バグを多量に含むかもしれません。

もしバグに気付いたらIssueかPRを投げてくれると助かります。