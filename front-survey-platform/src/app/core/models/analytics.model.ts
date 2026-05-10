export interface OptionResult {
  optionId: string;
  text: string;
  count: number;
  percentage: number;
}

export interface QuestionResult {
  questionId: string;
  text: string;
  type: 'SINGLE_CHOICE' | 'MULTIPLE_CHOICE' | 'NUMERIC' | 'TEXT' | 'YES_NO' | 'RATING';
  average: number | null;
  optionResults: OptionResult[];
}

export interface SurveyResults {
  surveyId: string;
  title: string;
  totalResponses: number;
  questionResults: QuestionResult[];
}
