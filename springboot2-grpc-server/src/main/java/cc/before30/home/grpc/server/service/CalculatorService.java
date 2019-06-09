package cc.before30.home.grpc.server.service;

import cc.before30.home.grpc.proto.CalculatorGrpc;
import cc.before30.home.grpc.proto.CalculatorOuterClass;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcService;

/**
 * CalculatorService
 *
 * @author before30
 * @since 2019-06-09
 */

@Slf4j
@GRpcService
public class CalculatorService extends CalculatorGrpc.CalculatorImplBase {
    @Override
    public void calculate(CalculatorOuterClass.CalculatorRequest request, StreamObserver<CalculatorOuterClass.CalculatorResponse> responseObserver) {
        CalculatorOuterClass.CalculatorResponse.Builder resultBuilder = CalculatorOuterClass.CalculatorResponse.newBuilder();
        switch (request.getOperation()){
            case ADD:
                resultBuilder.setResult(request.getNumber1()+request.getNumber2());
                break;
            case SUBTRACT:
                resultBuilder.setResult(request.getNumber1()-request.getNumber2());
                break;
            case MULTIPLY:
                resultBuilder.setResult(request.getNumber1()*request.getNumber2());
                break;
            case DIVIDE:
                resultBuilder.setResult(request.getNumber1()/request.getNumber2());
                break;
            case UNRECOGNIZED:
                break;
        }
        responseObserver.onNext(resultBuilder.build());
        responseObserver.onCompleted();
    }
}
