package de.lewens_markisen.log;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import de.lewens_markisen.repository.LogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class LogServiceImpl implements LogService{
	private final LogRepository logRepository;
	
	@Override
	public void save(Log log) {
		logRepository.save(log);
	}

	@Override
	public void deleteAltRecords() {
		List<Log> altRecords = logRepository.findAllByForCreationDate(LocalDate.now());
		altRecords.stream().forEach(l -> logRepository.delete(l));
	}

}
