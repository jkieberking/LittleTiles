package com.creativemd.littletiles.common.utils.grid;

public interface IGridBased {
//
    public LittleGridContext getContext();
//
//    @Deprecated
//    public default void forceContext(IGridBased other) {
//        if (getContext() != other.getContext()) {
//            if (getContext().size > other.getContext().size)
//                other.convertTo(getContext());
//            else
//                convertTo(other.getContext());
//        }
//    }
//
//    public default void ensureContext(IGridBased other, Runnable runnable) {
//        if (getContext() != other.getContext()) {
//            if (getContext().size > other.getContext().size)
//                other.convertTo(getContext());
//            else
//                convertTo(other.getContext());
//        }
//
//        runnable.run();
//
//        convertToSmallest();
//        other.convertToSmallest();
//    }
//
//    public default void convertToAtMinimum(LittleGridContext context) {
//        if (getContext().size < context.size)
//            convertTo(context);
//    }
//
    public void convertTo(LittleGridContext to);
//
//    public void convertToSmallest();

}
