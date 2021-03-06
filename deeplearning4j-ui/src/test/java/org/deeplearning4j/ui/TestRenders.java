package org.deeplearning4j.ui;

import org.deeplearning4j.datasets.fetchers.MnistDataFetcher;
import org.deeplearning4j.datasets.iterator.impl.IrisDataSetIterator;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.layers.factory.LayerFactories;
import org.deeplearning4j.nn.layers.feedforward.autoencoder.AutoEncoder;
import org.deeplearning4j.nn.params.PretrainParamInitializer;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.api.IterationListener;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.deeplearning4j.plot.PlotFilters;
import org.deeplearning4j.ui.activation.UpdateActivationIterationListener;
import org.deeplearning4j.ui.renders.UpdateFilterIterationListener;
import org.deeplearning4j.ui.weights.HistogramIterationListener;
import org.junit.Test;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.lossfunctions.LossFunctions;

import java.util.Arrays;
import java.util.Collections;


/**
 * @author Adam Gibson
 */
public class TestRenders extends BaseUiServerTest {
    @Test
    public void renderSetup() throws Exception {
        MnistDataFetcher fetcher = new MnistDataFetcher(true);
        NeuralNetConfiguration conf = new NeuralNetConfiguration.Builder().momentum(0.9f)
                .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
                .corruptionLevel(0.6)
                .iterations(100)
                .lossFunction(LossFunctions.LossFunction.RECONSTRUCTION_CROSSENTROPY)
                .learningRate(1e-1f).nIn(784).nOut(600)
                .layer(new org.deeplearning4j.nn.conf.layers.AutoEncoder())
                .build();


        fetcher.fetch(100);
        DataSet d2 = fetcher.next();

        INDArray input = d2.getFeatureMatrix();
        PlotFilters filters = new PlotFilters(input,new int[]{10,10},new int[]{0,0},new int[]{28,28});
        AutoEncoder da = LayerFactories.getFactory(conf.getLayer()).create(conf, Arrays.<IterationListener>asList(new ScoreIterationListener(1)
                ,new UpdateFilterIterationListener(filters,Collections.singletonList(PretrainParamInitializer.WEIGHT_KEY),1)),0);
        da.setParams(da.params());
        da.fit(input);
    }

    @Test
    public void testCoordUpdate() {

    }

    @Test
    public void renderActivation() throws Exception {
        MnistDataFetcher fetcher = new MnistDataFetcher(true);
        NeuralNetConfiguration conf = new NeuralNetConfiguration.Builder().momentum(0.9f)
                .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
                .corruptionLevel(0.6)
                .iterations(100)
                .lossFunction(LossFunctions.LossFunction.RECONSTRUCTION_CROSSENTROPY)
                .learningRate(1e-1f).nIn(784).nOut(600)
                .layer(new org.deeplearning4j.nn.conf.layers.AutoEncoder())
                .build();


        fetcher.fetch(100);
        DataSet d2 = fetcher.next();

        INDArray input = d2.getFeatureMatrix();
        AutoEncoder da = LayerFactories.getFactory(conf.getLayer()).create(conf, Arrays.<IterationListener>asList(new ScoreIterationListener(1),new UpdateActivationIterationListener(1)),0);
        da.setParams(da.params());
        da.fit(input);
    }

    @Test
    public void renderHistogram() throws Exception {
        MnistDataFetcher fetcher = new MnistDataFetcher(true);
        NeuralNetConfiguration conf = new NeuralNetConfiguration.Builder().momentum(0.9f)
                .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
                .corruptionLevel(0.6)
                .iterations(100).weightInit(WeightInit.XAVIER)
                .lossFunction(LossFunctions.LossFunction.RMSE_XENT)
                .learningRate(1e-1f).nIn(784).nOut(600)
                .layer(new org.deeplearning4j.nn.conf.layers.AutoEncoder())
                .build();


        fetcher.fetch(100);
        DataSet d2 = fetcher.next();

        INDArray input = d2.getFeatureMatrix();
        AutoEncoder da = LayerFactories.getFactory(conf.getLayer()).create(conf, Arrays.<IterationListener>asList(new ScoreIterationListener(1),new HistogramIterationListener(5)),0);
        da.setParams(da.params());
        da.fit(input);
    }

}
