package pl.edu.wat.wcy.isi.app.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.wat.wcy.isi.app.core.DistanceX;
import pl.edu.wat.wcy.isi.app.core.WeightDistribution;
import pl.edu.wat.wcy.isi.app.dto.MathematicalFunctionDTO;
import pl.edu.wat.wcy.isi.app.dto.message.request.GenerateDataSeriesForm;
import pl.edu.wat.wcy.isi.app.service.DownloadService;

import java.util.List;

@RestController
@RequestMapping(value = "/download")
@Slf4j
public class DownloadController {

    private final DownloadService downloadService;

    public DownloadController(DownloadService downloadService) {
        this.downloadService = downloadService;
    }

    @PutMapping(value = "/approximation", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> downloadApproximationResult(@RequestBody List<MathematicalFunctionDTO> mathematicalFunctionDTOs) {
        byte[] text = downloadService.getApproximationResult(mathematicalFunctionDTOs);
        log.debug("Downloading approximation results completed successfully.");
        return new ResponseEntity<>(text, HttpStatus.OK);
    }

    @PutMapping(value = "/generateDataSeries", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> generateDataSeries(@RequestBody GenerateDataSeriesForm dataSeriesForm) {
        WeightDistribution weightDistribution = WeightDistribution.valueOf(dataSeriesForm.getWeightDistribution().toUpperCase());
        DistanceX distanceX = DistanceX.valueOf(dataSeriesForm.getDistanceX().toUpperCase());
        byte[] text = downloadService.generateDataSeries(distanceX, weightDistribution, dataSeriesForm.getMathematicalFunctionDTO(), dataSeriesForm.getNumberPoints(), dataSeriesForm.getNoise());
        log.debug("Generating data series completed successfully.");
        return new ResponseEntity<>(text, HttpStatus.OK);
    }
}