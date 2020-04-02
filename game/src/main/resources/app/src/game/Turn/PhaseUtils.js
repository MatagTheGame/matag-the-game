export class PhaseUtils {
  static getPhases() {
    return ['UT', 'UP', 'DR', 'M1', 'BC', 'DA', 'DB', 'AB', 'FS', 'CD', 'EC', 'M2', 'ET', 'CL']
  }

  static isMainPhase(phase) {
    return phase === 'M1' || phase === 'M2'
  }
}