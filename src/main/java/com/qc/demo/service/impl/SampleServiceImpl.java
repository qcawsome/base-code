package com.qc.demo.service.impl;

import com.qc.demo.domain.Sample;
import com.qc.demo.repository.SampleRepository;
import com.qc.demo.service.SampleService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Sample}.
 */
@Service
@Transactional
public class SampleServiceImpl implements SampleService {

    private final Logger log = LoggerFactory.getLogger(SampleServiceImpl.class);

    private final SampleRepository sampleRepository;

    public SampleServiceImpl(SampleRepository sampleRepository) {
        this.sampleRepository = sampleRepository;
    }

    @Override
    public Sample save(Sample sample) {
        log.debug("Request to save Sample : {}", sample);
        return sampleRepository.save(sample);
    }

    @Override
    public Sample update(Sample sample) {
        log.debug("Request to update Sample : {}", sample);
        return sampleRepository.save(sample);
    }

    @Override
    public Optional<Sample> partialUpdate(Sample sample) {
        log.debug("Request to partially update Sample : {}", sample);

        return sampleRepository
            .findById(sample.getId())
            .map(existingSample -> {
                if (sample.getText() != null) {
                    existingSample.setText(sample.getText());
                }

                return existingSample;
            })
            .map(sampleRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Sample> findAll(Pageable pageable) {
        log.debug("Request to get all Samples");
        return sampleRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Sample> findOne(Long id) {
        log.debug("Request to get Sample : {}", id);
        return sampleRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Sample : {}", id);
        sampleRepository.deleteById(id);
    }
}
