import Shared


// Kotlin Enum -> Swift Enum
// Kotlin Sealed class -> Swift Enum with associated value

func testSealedClass(status: Status) {
    switch onEnum(of: status) {
    case .loading:
        print("loading...")
    case .error(let data):
        print("Error: \(data.message)")
    case .success(let data):
        print("Success: \(data.result)")
    }
}
