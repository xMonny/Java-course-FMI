package bg.sofia.uni.fmi.mjt.warehouse;

import bg.sofia.uni.fmi.mjt.warehouse.exceptions.CapacityExceededException;
import bg.sofia.uni.fmi.mjt.warehouse.exceptions.ParcelNotFoundException;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.HashMap;

public class MJTExpressWarehouse<L, P> implements DeliveryServiceWarehouse<L, P> {
    private final int capacity;
    private int freeSpace;
    private final int retentionPeriod;
    private final Map<L, Pair<P, LocalDateTime>> warehouseStorage;

    public MJTExpressWarehouse(int capacity, int retentionPeriod) {
        this.capacity = capacity;
        this.freeSpace = capacity;
        this.retentionPeriod = retentionPeriod;
        warehouseStorage = new HashMap<>(capacity);
    }

    private boolean isFull() {
        return this.freeSpace == 0;
    }

    private void increaseFreeSpace() {
        this.freeSpace++;
    }

    private void decreaseFreeSpace() {
        this.freeSpace--;
    }

    private boolean isParcelSubmitted(L label) {
        return warehouseStorage.containsKey(label);
    }

    @Override
    public void submitParcel(L label, P parcel, LocalDateTime submissionDate) throws CapacityExceededException {
        if (label == null || parcel == null || submissionDate == null) {
            throw new IllegalArgumentException("Cannot submit parcel: Null argument is passed");
        }
        if (submissionDate.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Cannot submit parcel: Input date is in the future");
        }
        if (!isParcelSubmitted(label)) {
            if (isFull()) {
                deliverParcelsSubmittedBefore(LocalDateTime.now().minusDays(retentionPeriod));
            }
            if (isFull()) {
                throw new CapacityExceededException("Cannot submit parcel: Storage is full");
            }
            warehouseStorage.put(label, new Pair<>(parcel, submissionDate));
            decreaseFreeSpace();
        }
    }

    @Override
    public P getParcel(L label) {
        if (label == null) {
            throw new IllegalArgumentException("Cannot get parcel: Null label is passed");
        }
        if (!isParcelSubmitted(label)) {
            return null;
        }
        Pair<P, LocalDateTime> innerStorage = warehouseStorage.get(label);
        return innerStorage.key();
    }

    @Override
    public P deliverParcel(L label) throws ParcelNotFoundException {
        if (label == null) {
            throw new IllegalArgumentException("Cannot deliver parcel: Null label is passed");
        }
        if (!isParcelSubmitted(label)) {
            throw new ParcelNotFoundException("Cannot deliver parcel: Label \"" + label + "\" doesn't exist");
        }
        P parcelToDeliver = getParcel(label);
        warehouseStorage.remove(label);
        increaseFreeSpace();
        return parcelToDeliver;
    }

    @Override
    public double getWarehouseSpaceLeft() {
        double leftSpace = (double) freeSpace / (double) capacity;
        return Math.round(leftSpace * 100.0) / 100.0;
    }

    @Override
    public Map<L, P> getWarehouseItems() {
        Map<L, P> itemsInStorage = new HashMap<>();
        for (L lab : warehouseStorage.keySet()) {
            Pair<P, LocalDateTime> innerStorage = warehouseStorage.get(lab);
            P parcel = innerStorage.key();
            itemsInStorage.put(lab, parcel);
        }
        return itemsInStorage;
    }

    @Override
    public Map<L, P> deliverParcelsSubmittedBefore(LocalDateTime before) {
        if (before == null) {
            throw new IllegalArgumentException("Cannot deliver parcels submitted before: Null date is passed");
        }
        Map<L, P> parcelsSubmittedBefore = new HashMap<>();
        if (before.isAfter(LocalDateTime.now())) {
            return getWarehouseItems();
        }
        for (L lab : warehouseStorage.keySet()) {
            Pair<P, LocalDateTime> innerStorage = warehouseStorage.get(lab);
            P parcel = innerStorage.key();
            LocalDateTime parcelDateTime = innerStorage.value();
            if (parcelDateTime.isBefore(before)) {
                parcelsSubmittedBefore.put(lab, parcel);
                increaseFreeSpace();
            }
        }
        for (L lab : parcelsSubmittedBefore.keySet()) {
            warehouseStorage.remove(lab);
        }
        return parcelsSubmittedBefore;
    }

    @Override
    public Map<L, P> deliverParcelsSubmittedAfter(LocalDateTime after) {
        if (after == null) {
            throw new IllegalArgumentException("Cannot deliver parcels submitted after: Null date is passed");
        }
        Map<L, P> parcelsSubmittedAfter = new HashMap<>();
        if (!after.isAfter(LocalDateTime.now())) {
            for (L lab : warehouseStorage.keySet()) {
                Pair<P, LocalDateTime> innerStorage = warehouseStorage.get(lab);
                P parcel = innerStorage.key();
                LocalDateTime parcelDateTime = innerStorage.value();
                if (parcelDateTime.isAfter(after)) {
                    parcelsSubmittedAfter.put(lab, parcel);
                    increaseFreeSpace();
                }
            }
            for (L lab : parcelsSubmittedAfter.keySet()) {
                warehouseStorage.remove(lab);
            }
        }
        return parcelsSubmittedAfter;
    }
}
