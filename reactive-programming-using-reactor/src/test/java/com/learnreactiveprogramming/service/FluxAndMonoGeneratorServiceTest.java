package com.learnreactiveprogramming.service;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

class FluxAndMonoGeneratorServiceTest {
    FluxAndMonoGeneratorService fluxAndMonoGeneratorService = new FluxAndMonoGeneratorService();

    @Test
    void namesFlux() {
        //given

        //when
        Flux<String> namesFlux = fluxAndMonoGeneratorService.namesFlux();

        //then
        StepVerifier.create(namesFlux)
//                .expectNext("alex", "ben", "chloe")
                .expectNext("alex")
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    void namesFlux_map() {
        //given
        int stringLength = 3;

        //when
        Flux<String> namesFlux = fluxAndMonoGeneratorService.namesFlux_map(stringLength);

        //then
        StepVerifier.create(namesFlux)
                .expectNext("4-ALEX", "5-CHLOE")
//                .expectNext("ALEX")
//                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    void namesFlux_immutability() {
        //given

        //when
        Flux<String> namesFlux = fluxAndMonoGeneratorService.namesFlux_immutability();

        //then
        StepVerifier.create(namesFlux)
                .expectNext("alex", "ben", "chloe")
                .verifyComplete();
    }

    @Test
    void namesFlux_flatmap() {
        //given
        int stringLength = 3;

        //when
//        Flux<String> namesFlux = fluxAndMonoGeneratorService.namesFlux_flatmap(stringLength);
        var namesFlux = fluxAndMonoGeneratorService.namesFlux_flatmap(stringLength);

        //then
        StepVerifier.create(namesFlux)
                .expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E")
                .verifyComplete();
    }

    @Test
    void namesFlux_flatmap_async() {
        //given
        int stringLength = 3;

        //when
        Flux<String> namesFlux = fluxAndMonoGeneratorService.namesFlux_flatmap_async(stringLength);

        //then
        StepVerifier.create(namesFlux)
//                .expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E")
                .expectNextCount(9)
                .verifyComplete();

    }

    @Test
    void namesFlux_concatmap() {
        //given
        int stringLength = 3;

        //when
        Flux<String> namesFlux = fluxAndMonoGeneratorService.namesFlux_concatmap(stringLength);

        //then
        StepVerifier.create(namesFlux)
                .expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E")
//                .expectNextCount(9)

                .verifyComplete();
    }

    @Test
    void namesMono_map_flatMap() {
        //given
        int stringLength = 3;

        //when
        Mono<List<String>> value = fluxAndMonoGeneratorService.namesMono_map_flatMap(stringLength);

        //then
        StepVerifier.create(value)
                .expectNext(List.of("A", "L", "E", "X"))
                .verifyComplete();
    }

    @Test
    void namesMono_map_flatMapMany() {
        //given
        int stringLength = 3;

        //when
        Flux<String> value = fluxAndMonoGeneratorService.namesMono_map_flatMapMany(stringLength);

        //then
        StepVerifier.create(value)
                .expectNext("A", "L", "E", "X")
                .verifyComplete();
    }

    @Test
    void namesFlux_transform() {
        //given
        int stringLength = 3;

        //when
//        Flux<String> namesFlux = fluxAndMonoGeneratorService.namesFlux_flatmap(stringLength);
        var namesFlux = fluxAndMonoGeneratorService.namesFlux_transform(stringLength);

        //then
        StepVerifier.create(namesFlux)
                .expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E")
                .verifyComplete();
    }

    @Test
    void namesFlux_transform_1() {
        //given
        int stringLength = 6;

        //when
//        Flux<String> namesFlux = fluxAndMonoGeneratorService.namesFlux_flatmap(stringLength);
        var namesFlux = fluxAndMonoGeneratorService.namesFlux_transform(stringLength);

        //then
        StepVerifier.create(namesFlux)
//                .expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E")
                .expectNext("default")
                .verifyComplete();
    }

    @Test
    void namesFlux_transform_switchifEmpty() {
        //given
        int stringLength = 6;

        //when
//        Flux<String> namesFlux = fluxAndMonoGeneratorService.namesFlux_transform_switchifEmpty(stringLength);
        var namesFlux = fluxAndMonoGeneratorService.namesFlux_transform_switchifEmpty(stringLength);

        //then
        StepVerifier.create(namesFlux)
//                .expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E")
                .expectNext("D", "E", "F", "A", "U", "L", "T")
                .verifyComplete();
    }

    @Test
    void explore_concat() {
        // given

        // when
        var concatFlux = fluxAndMonoGeneratorService.explore_concat();

        // then
        StepVerifier.create(concatFlux)
                .expectNext("A", "B", "C", "D", "E", "F")
                .verifyComplete();
    }

    @Test
    void explore_merge() {
        // given

        // when
        var value = fluxAndMonoGeneratorService.explore_merge();

        // then
        StepVerifier.create(value)
                .expectNext("A", "D", "B", "E", "C", "F")
                .verifyComplete();
    }

    @Test
    void explore_mergeSequential() {
        // given

        // when
        var value = fluxAndMonoGeneratorService.explore_mergeSequential();

        // then
        StepVerifier.create(value)
                .expectNext("A", "B", "C", "D", "E", "F")
                .verifyComplete();
    }

    @Test
    void explore_zip() {
        // given

        // when
        var value = fluxAndMonoGeneratorService.explore_zip();

        // then
        StepVerifier.create(value)
                .expectNext("AD", "BE", "CF")
                .verifyComplete();
    }

    @Test
    void explore_zip_1() {
        // given

        // when
        var value = fluxAndMonoGeneratorService.explore_zip_1();

        // then
        StepVerifier.create(value)
                .expectNext("AD14", "BE25", "CF36")
                .verifyComplete();
    }
}