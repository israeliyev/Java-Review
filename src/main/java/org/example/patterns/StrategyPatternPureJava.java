package org.example.patterns;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

enum CompressionType {
    ZIP,
    GZIP,
    RAR
}

interface CompressionStrategy {

    byte[] compress(byte[] data);

    CompressionType supportedType();
}

class ZipCompressionStrategy implements CompressionStrategy{

    @Override
    public byte[] compress(byte[] data) {
        return new byte[0];
    }

    @Override
    public CompressionType supportedType() {
        return CompressionType.ZIP;
    }
}

class GzipCompressionStrategy implements CompressionStrategy{

    @Override
    public byte[] compress(byte[] data) {
        return new byte[0];
    }

    @Override
    public CompressionType supportedType() {
        return CompressionType.GZIP;
    }
}

class RarCompressionStrategy implements CompressionStrategy{

    @Override
    public byte[] compress(byte[] data) {
        return new byte[0];
    }

    @Override
    public CompressionType supportedType() {
        return CompressionType.RAR;
    }
}

class CompressionStrategyResolver{
    private final Map<CompressionType, CompressionStrategy> strategies;


    CompressionStrategyResolver(List<CompressionStrategy> strategies) {
        this.strategies = strategies.stream().collect(Collectors.toUnmodifiableMap(CompressionStrategy::supportedType, Function.identity()));
    }

    public CompressionStrategy get(CompressionType compressionType){
        return this.strategies.get(compressionType);
    }
}

class FileCompressionService {

    ZipCompressionStrategy zipCompressionStrategy = new ZipCompressionStrategy();
    GzipCompressionStrategy gzipCompressionStrategy = new GzipCompressionStrategy();
    RarCompressionStrategy rarCompressionStrategy = new RarCompressionStrategy();

    CompressionStrategyResolver compressionStrategyResolver = new CompressionStrategyResolver(List.of(zipCompressionStrategy, gzipCompressionStrategy, rarCompressionStrategy));

    byte[] compress(CompressionType compressionType, byte[] data){
        CompressionStrategy compressionStrategy = compressionStrategyResolver.get(compressionType);

        if (Objects.isNull(compressionStrategy)){
            throw new RuntimeException("compression Strategy is not found !!");
        }

         return compressionStrategy.compress(data);
    }
}

public class StrategyPatternPureJava {
}
