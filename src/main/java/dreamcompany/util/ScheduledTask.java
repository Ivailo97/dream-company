package dreamcompany.util;

import dreamcompany.repository.TeamRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@EnableAsync
@AllArgsConstructor
public class ScheduledTask {

    private final TeamRepository teamRepository;

    private final AtomicInteger invocationsCount;

    //executed at 10:15 AM on the 15th day of every month.
    @Async
    @Scheduled(cron = "0 15 10 15 * ?")
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
