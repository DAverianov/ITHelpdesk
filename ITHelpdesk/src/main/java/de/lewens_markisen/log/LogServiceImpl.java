package de.lewens_markisen.log;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import de.lewens_markisen.domain.localDb.Log;
import de.lewens_markisen.repository.local.LogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class LogServiceImpl implements LogService{
	private final LogRepository logRepository;
	@Value("${logging_in_DB.saving_days}")
	private Integer savingDays;
	
	@Override
	public void save(Log log) {
		logRepository.save(log);
	}

	@Override
	public void deleteAltRecords() {
		LocalDate grenze = LocalDate.now();
		grenze = grenze.minusDays((long) savingDays);
		List<Log> altRecords = logRepository.findAllByForCreationDate(grenze);
		altRecords.stream().forEach(l -> logRepository.delete(l));
	}

}
