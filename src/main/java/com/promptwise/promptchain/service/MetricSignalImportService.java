package com.promptwise.promptchain.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.promptwise.promptchain.common.util.json.JacksonUtil;
import com.promptwise.promptchain.entity.RawMetrics;
import com.promptwise.promptchain.model.DoubleGaugeMetricDataPoint;
import com.promptwise.promptchain.model.DoubleSumMetricDataPoint;
import com.promptwise.promptchain.model.GaugeMetric;
import com.promptwise.promptchain.model.GaugeMetricDetail;
import com.promptwise.promptchain.model.HistogramMetric;
import com.promptwise.promptchain.model.HistogramMetricDataPoint;
import com.promptwise.promptchain.model.HistogramMetricDetail;
import com.promptwise.promptchain.model.IntegerGaugeMetricDataPoint;
import com.promptwise.promptchain.model.IntegerSumMetricDataPoint;
import com.promptwise.promptchain.model.Metric;
import com.promptwise.promptchain.model.MetricDataPoint;
import com.promptwise.promptchain.model.MetricDetail;
import com.promptwise.promptchain.model.MetricSignal;
import com.promptwise.promptchain.model.MetricSignals;
import com.promptwise.promptchain.model.NumberMetricDataPoint;
import com.promptwise.promptchain.model.ScopeMetrics;
import com.promptwise.promptchain.model.SumMetric;
import com.promptwise.promptchain.model.SumMetricDataPoint;
import com.promptwise.promptchain.model.SumMetricDetail;
import com.promptwise.promptchain.model.SummaryMetric;
import com.promptwise.promptchain.model.SummaryMetricDataPoint;
import com.promptwise.promptchain.model.SummaryMetricDetail;
import com.promptwise.promptchain.repository.RawMetricsRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

//TODO: Implement processing of metrics

@Service
public class MetricSignalImportService implements TelemetrySignalImportService<MetricSignal> {

  @Autowired
  RawMetricsRepository rawMetricsRepository;

  public void processSignals(MetricSignals metricSignals) {
    for (MetricSignal metricSignal : metricSignals.getMetricSignals()) {
      processSignal(metricSignal);
    }
  }

  public void processSignal(MetricSignal metricSignal) {
    for (ScopeMetrics scopeMetrics : metricSignal.getScopeSignals()) {
      for (Metric<? extends MetricDetail<? extends MetricDataPoint>> metric : scopeMetrics.getSignals()) {
        switch (metric.getType()) {
          case "GAUGE" -> processGaugeMetric((GaugeMetric) metric);
          case "SUM" -> processSumMetric((SumMetric) metric);
          case "HISTOGRAM" -> processHistogramMetric((HistogramMetric) metric);
          case "SUMMARY" -> processSummaryMetric((SummaryMetric) metric);
          default -> throw new IllegalArgumentException("Unknown metric type: " + metric.getType());
        }
      }
    }
  }

  protected void processGaugeMetric(GaugeMetric gaugeMetric) {
    GaugeMetricDetail gaugeMetricDetail = gaugeMetric.getMetricDetail();
    for (NumberMetricDataPoint numberMetricDataPoint : gaugeMetricDetail.getNumberMetricDataPoints()) {
      if (numberMetricDataPoint instanceof DoubleGaugeMetricDataPoint) {
        processDoubleGaugeMetricDataPoint((DoubleGaugeMetricDataPoint) numberMetricDataPoint);
      } else if (numberMetricDataPoint instanceof IntegerGaugeMetricDataPoint) {
        processIntegerGaugeMetricDataPoint((IntegerGaugeMetricDataPoint) numberMetricDataPoint);
      }
    }
  }

  protected void processDoubleGaugeMetricDataPoint(DoubleGaugeMetricDataPoint doubleGaugeMetricDataPoint) {
  }

  protected void processIntegerGaugeMetricDataPoint(IntegerGaugeMetricDataPoint integerGaugeMetricDataPoint) {
  }

  protected void processSumMetric(SumMetric sumMetric) {
    SumMetricDetail sumMetricDetail = sumMetric.getMetricDetail();
    for (SumMetricDataPoint sumMetricDataPoint : sumMetricDetail.getSumMetricDataPoints()) {
      if (sumMetricDataPoint instanceof DoubleSumMetricDataPoint) {
        processDoubleSumMetricDataPoint((DoubleSumMetricDataPoint) sumMetricDataPoint);
      } else if (sumMetricDataPoint instanceof IntegerSumMetricDataPoint) {
        processIntegerSumMetricDataPoint((IntegerSumMetricDataPoint) sumMetricDataPoint);
      }
    }
  }

  protected void processDoubleSumMetricDataPoint(DoubleSumMetricDataPoint doubleSumMetricDataPoint) {
  }

  protected void processIntegerSumMetricDataPoint(IntegerSumMetricDataPoint integerSumMetricDataPoint) {
  }

  protected void processHistogramMetric(HistogramMetric histogramMetric) {
    HistogramMetricDetail histogramMetricDetail = histogramMetric.getMetricDetail();
    for (HistogramMetricDataPoint histogramMetricDataPoint : histogramMetricDetail.getHistogramMetricDataPoints()) {
    }
  }

  protected void processSummaryMetric(SummaryMetric summaryMetric) {
    SummaryMetricDetail summaryMetricDetail = summaryMetric.getMetricDetail();
    for (SummaryMetricDataPoint summaryMetricDataPoint : summaryMetricDetail.getSummaryMetricDataPoints()) {
    }
  }

  /**
   * This method would import the metrics payload into raw_metrics table
   * @param metricSignals
   * @param orgId
   * @param clientAppId
   */
  @Transactional
  public void importRawMetrics(MetricSignals metricSignals, Long orgId, Long clientAppId) {
    try {

      // Serialize the entire MetricSignals object into JSON
      JsonNode jsonPayload = JacksonUtil.getInstance().getObjectMapper().valueToTree(metricSignals);
      // Fetching nextId as we are using composite key which is not supported to be autogenerated
      Long nextId = rawMetricsRepository.getNextId();

      // Create the entity
      RawMetrics entity = new RawMetrics(
              nextId,
              orgId,
              clientAppId,
              jsonPayload,
              OffsetDateTime.now()
      );
      // Persist
      rawMetricsRepository.save(entity);

    } catch (Exception e) {
      throw new RuntimeException("Failed to import raw metrics", e);
    }
  }

}
