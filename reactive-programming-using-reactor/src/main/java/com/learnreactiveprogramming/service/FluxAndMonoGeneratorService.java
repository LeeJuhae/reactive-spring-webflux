package com.learnreactiveprogramming.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class FluxAndMonoGeneratorService {

    public Flux<String> namesFlux() {
        return Flux.fromIterable(List.of("alex", "ben", "chloe")).log(); // db or a remote service call
    }

    public Mono<String> namesMono() {
        return Mono.just("alex").log();
    }

    public Flux<String> namesFlux_map(int stringLength) {
        // filter the string whose length is greater than 3
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .map(String::toUpperCase)
                .filter(s -> s.length() > stringLength)
                .map(s -> s.length() + "-"+s)
                .log();
    }

    public Flux<String> namesFlux_immutability() {
        Flux<String> namesFlux = Flux.fromIterable(List.of("alex", "ben", "chloe"));

        namesFlux.map(String::toUpperCase);

        return namesFlux;
    }

    public Mono<String> namesMono_map_filter(int stringLength) {
        // filter the string whose length is greater than 3
        return Mono.just("alex")
                .map(String::toUpperCase)
                .filter(s -> s.length() > stringLength);
    }

    public Mono<List<String>> namesMono_map_flatMap(int stringLength) {
        return Mono.just("alex")
                .map(String::toUpperCase)
                .filter(s -> s.length() > stringLength)
                .flatMap(this::splitStringMono) // Mono<List of A, L, E, X>
                ;
    }

    public Flux<String> namesMono_map_flatMapMany(int stringLength) {
        return Mono.just("alex")
                .map(String::toUpperCase)
                .filter(s -> s.length() > stringLength)
                .flatMapMany(this::splitString) // Mono<List of A, L, E, X>
                ;
    }

    private Mono<List<String>> splitStringMono(String s) {
        String[] charArray = s.split("");
        List<String> charList = List.of(charArray);
        return Mono.just(charList);
    }
    public Flux<String> namesFlux_flatmap(int stringLength) {
        // filter the string whose length is greater than 3
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .map(String::toUpperCase)
                .filter(s -> s.length() > stringLength)
                // ALEX, CHLOE -> A, L, E, X, C, H, L, O, E
                .flatMap(s -> splitString(s)) // A, L, E, X, C, H, L, O, E
                .log();
    }

    public Flux<String> namesFlux_flatmap_async(int stringLength) {
        // filter the string whose length is greater than 3
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .map(String::toUpperCase)
                .filter(s -> s.length() > stringLength)
                // ALEX, CHLOE -> A, L, E, X, C, H, L, O, E
                .flatMap(s -> splitString_withDelay(s)) // A, L, E, X, C, H, L, O, E
                .log();
    }

    public Flux<String> namesFlux_concatmap(int stringLength) {
        // filter the string whose length is greater than 3
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .map(String::toUpperCase)
                .filter(s -> s.length() > stringLength)
                // ALEX, CHLOE -> A, L, E, X, C, H, L, O, E
                .concatMap(s -> splitString(s)) // A, L, E, X, C, H, L, O, E
                .log();
    }

    public Flux<String> namesFlux_transform(int stringLength) {
        // filter the string whose length is greater than 3

        Function<Flux<String>, Flux<String>> filtermap = name ->
                name.map(String::toUpperCase).filter(s -> s.length() > stringLength);

        // Flux.empty()
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .transform(filtermap)
                .flatMap(s -> splitString(s)) // A, L, E, X, C, H, L, O, E
                .defaultIfEmpty("default")
                .log();
    }

    public Flux<String> namesFlux_transform_switchifEmpty(int stringLength) {
        // filter the string whose length is greater than 3

        Function<Flux<String>, Flux<String>> filtermap = name ->
                name.map(String::toUpperCase).filter(s -> s.length() > stringLength)
                        .flatMap(s -> splitString(s));

        var defaultFlux = Flux.just("default").transform(filtermap); // "D", "E", "F", "A", "U", "L", "T"

        // Flux.empty()
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .transform(filtermap)
                .switchIfEmpty(defaultFlux)
                .log();
    }

    public Flux<String> explore_concat() {

        var abcFlux = Flux.just("A", "B", "C");
        var defFlux = Flux.just("D", "E", "F");

        return Flux.concat(abcFlux, defFlux);
    }

    public Flux<String> explore_concatwith() {

        var abcFlux = Flux.just("A", "B", "C");
        var defFlux = Flux.just("D", "E", "F");

        return abcFlux.concatWith(defFlux);
    }

    public Flux<String> explore_concatwith_mono() {

        var aMono = Mono.just("A");
        var bMono = Flux.just("B");

        return aMono.concatWith(bMono).log(); // A, B
    }

    public Flux<String> explore_merge() {

        var abcFlux = Flux.just("A", "B", "C").delayElements(Duration.ofMillis(100));
        var defFlux = Flux.just("D", "E", "F").delayElements(Duration.ofMillis(125));

        return Flux.merge(abcFlux, defFlux).log();
    }

    public Flux<String> explore_mergeWith() {

        var abcFlux = Flux.just("A", "B", "C").delayElements(Duration.ofMillis(100));
        var defFlux = Flux.just("D", "E", "F").delayElements(Duration.ofMillis(125));

//        return Flux.merge(abcFlux, defFlux).log();
        return abcFlux.mergeWith(defFlux).log();
    }

    public Flux<String> explore_mergeWith_momo() {

        var aMono = Mono.just("A"); // A
        var bMono = Mono.just("B"); // B

        return aMono.mergeWith(bMono).log(); // A, B
    }

    public Flux<String> explore_mergeSequential() {

        var abcFlux = Flux.just("A", "B", "C").delayElements(Duration.ofMillis(100));
        var defFlux = Flux.just("D", "E", "F").delayElements(Duration.ofMillis(125));

//        return Flux.merge(abcFlux, defFlux).log();
        return Flux.mergeSequential(abcFlux, defFlux).log();
    }

    public Flux<String> explore_zip() {

        var abcFlux = Flux.just("A", "B", "C");
        var defFlux = Flux.just("D", "E", "F");

        return Flux.zip(abcFlux, defFlux, (first, second) -> first + second).log();
    }

    public Flux<String> explore_zip_1() {

        var abcFlux = Flux.just("A", "B", "C");
        var defFlux = Flux.just("D", "E", "F");
        var _123Flux = Flux.just("1", "2", "3");
        var _456lux = Flux.just("4", "5", "6");

        return Flux.zip(abcFlux, defFlux, _123Flux, _456lux)
                .map(t4 -> t4.getT1() + t4.getT2() + t4.getT3() + t4.getT4())
                .log();
    }

    public Flux<String> explore_zipWith() {

        var abcFlux = Flux.just("A", "B", "C");
        var defFlux = Flux.just("D", "E", "F");

        return abcFlux.zipWith(defFlux, (first, second) -> first + second).log(); // AD, BE, CF
    }

    public Mono<String> explore_mergeZipWith_momo() {

        var aMono = Mono.just("A"); // A
        var bMono = Mono.just("B"); // B

        return aMono.zipWith(bMono)
                .map(t2 -> t2.getT1() + t2.getT2())
                .log(); // A, B
    }

    public Flux<String> splitString(String name) {
        String[] charArray = name.split("");
        return Flux.fromArray(charArray);
    }

    public Flux<String> splitString_withDelay(String name) {
        String[] charArray = name.split("");
        int delay = new Random().nextInt(1000);
        return Flux.fromArray(charArray).delayElements(Duration.ofMillis(delay));
    }

    public static void main(String[] args) {
        FluxAndMonoGeneratorService fluxAndMonoGeneratorService = new FluxAndMonoGeneratorService();

        fluxAndMonoGeneratorService.namesFlux().subscribe(name -> {
            System.out.println("Name is : " + name);
        });

        fluxAndMonoGeneratorService.namesMono().subscribe(name -> {
            System.out.println("Mono Name is : " + name);
        });
    }
}
