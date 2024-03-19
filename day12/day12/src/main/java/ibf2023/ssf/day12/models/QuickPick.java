package ibf2023.ssf.day12.models;

// record class - hold values only, don't have methods
// immutable - once created, cannot change!!
public record QuickPick(
        int startNum,
        int endNum,
        int count) {

}
