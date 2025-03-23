package com.aki.akisutils.apis.util.list;

public class Pair<K, V>
{
    private final K key;
    private final V value;

    //指定された値で新しいペアを構築します
    public Pair(K key, V value)
    {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Pair<?, ?> pair = (Pair<?, ?>) o;

        //基になるオブジェクトの`equals()`メソッドを呼び出します
        if (!key.equals(pair.key)) {
            return false;
        }
        return value.equals(pair.value);
    }

    @Override
    //ハッシュテーブルをサポートするオブジェクトのハッシュコードを計算します
    public int hashCode()
    {
        //基になるオブジェクトのハッシュコードを使用します
        return 31 * key.hashCode() + value.hashCode();
    }

    @Override
    public String toString() {
        return "(" + key + ", " + value + ")";
    }

    //型付きペア不変インスタンスを作成するためのファクトリメソッド
    public static <U, V> Pair <U, V> of(U a, V b)
    {
        //プライベートコンストラクタを呼び出します
        return new Pair<>(a, b);
    }
}
