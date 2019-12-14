package dreamcompany.util;

import dreamcompany.repository.TeamRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;

@EnableAsync
@AllArgsConstructor
public class ScheduledTask {

    private final TeamRepository teamRepository;

    private final AtomicInteger invocationsCount;

    //Every Day at 5:30 AM
    @Async
    @Scheduled(cron = "30 5 * * *")
    public void payTaxesEveryTenMinutes() {
        BigDecimal tax = BigDecimal.valueOf(20);

        teamRepository.findAllByProfitAfter(tax).forEach(t -> {
            t.setProfit(t.getProfit().subtract(tax));
            teamRepository.save(t);
        });

        invocationsCount.incrementAndGet();
    }

    public AtomicInteger getInvocationsCount() {
        return invocationsCount;
    }
}
