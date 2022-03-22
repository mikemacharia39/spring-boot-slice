package com.mikehenry.springbootslice.service;

import com.mikehenry.springbootslice.model.Address;
import com.mikehenry.springbootslice.repository.AddressPK;
import com.mikehenry.springbootslice.repository.AddressRepository;
import com.mikehenry.springbootslice.util.Benchmark;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;
    private final Random random = new Random();
    private static final ExecutorService executor = Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors() - 1);
    private final TransactionTemplate txTemplate;

    @Async
    public void saveAll() {
        log.info("Saving all addresses");
        List<String> addressList = Arrays.asList("Manchester FCI", "Marianna FCI", "Marion USP", "McCreary USP", "McDowell FCI", "McKean FCI", "McRae CI", "Memphis FCI", "Mendota FCI", "Miami FCI", "Miami FDC", "Miami RRM", "Mid-Atlantic RO", "Milan FCI", "Minneapolis RRM", "Montgomery FPC", "Montgomery RRM", "Morgantown FCI", "MSTC", "Sacramento RRM", "Safford FCI", "San Antonio RRM", "San Diego MCC", "Sandstone FCI", "Schuylkill FCI", "Seagoville FCI", "SeaTac FDC", "Seattle RRM", "Sheridan FCI", "South Central RO", "Southeast RO", "Springfield MCFP", "St Louis RRM", "Baltimore RRM", "Bastrop FCI", "Beaumont Low FCI", "Beaumont Medium FCI", "Beaumont USP", "Beckley FCI", "Bennettsville FCI", "Berlin FCI", "Big Sandy USP", "Big Spring FCI", "Brooklyn MDC", "Bryan FPC", "Butner Low FCI", "Butner Medium I FCI", "Butner Medium II FCI", "Butner FMC");
        List<Address> addresses = new ArrayList<>();
        long currentStartTime = System.currentTimeMillis();
        Random randomId = new Random(123456789);

        int limit = 10000;
        for (int i = 1; i <= limit; i++) {
            String location = addressList.get(random.nextInt(addressList.size()));

            Address address = new Address(new AddressPK("sub " + location + " " + i,
                    location + " - " + i + "-" + randomId.nextInt(),
                    location + " - " + i + "-" + randomId.nextInt()),
                    "Po Box " + randomId.nextInt());
            addresses.add(address);
        }

        log.info("Before save :: For {} records it took {} to prepare batch", limit, Benchmark.getTAT(currentStartTime));

        CompletableFuture.runAsync(() -> txTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                addressRepository.saveAll(addresses);
                addresses.clear();
                log.info("After save :: For {} records it took {}", limit, Benchmark.getTAT(currentStartTime));
            }
        }), executor);
    }
}
