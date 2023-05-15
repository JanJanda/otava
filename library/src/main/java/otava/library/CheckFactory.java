package otava.library;

interface CheckFactory {
    <T extends Check> Check getInstance(Class<T> type) throws ValidatorException;
}
