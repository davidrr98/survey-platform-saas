import { QuestionType } from './enums/question-type.enum';

export interface Option {
  id?: string;
  text: string;
  optionOrder: number;
}

export interface Question {
  id?: string;
  text: string;
  type: QuestionType;
  questionOrder: number;
  required: boolean;
  surveyId?: string;
  options?: Option[];
}

export interface QuestionFormData {
  text: string;
  type: QuestionType;
  questionOrder: number;
  required: boolean;
  options?: Option[];
}