package ia.neuralnetwork.transferfunctions;

import ia.neuralnetwork.interfaces.TransferFunction;

public enum TransferFunctions implements TransferFunction {
    //
    GAUSSIAN() {

        /**
         * Gaussian Transfer function
         *
         * @param x
         * @return
         */
        @Override
        public float transfer(float x) {
            float a = 1;
            return (float)(1.0f / Math.sqrt(2 * Math.PI * a)) * (float)Math.pow(Math.E, - ((x*x) / (2*a*a)));
        }
    },

    SIGMOID() {

        /**
         * Sigmoid transfer function
         * @param x
         * @return
         */
        @Override
        public float transfer(float x) {
            return (float) (1.0f / (1 + Math.pow(Math.E, -x)));
        }
    },

    LINEAR() {

        /**
         * Linear transfer functioon
         * @param x
         * @return
         */
        @Override
        public float transfer(float x) {
            return x;
        }
    };

    //
    TransferFunctions() {}
}
